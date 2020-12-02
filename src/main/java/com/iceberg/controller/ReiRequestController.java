package com.iceberg.controller;

import static com.iceberg.entity.ReimbursementRequest.Status.APPROVED;
import static com.iceberg.entity.ReimbursementRequest.Status.DENIED;
import static com.iceberg.entity.ReimbursementRequest.Status.PROCESSING;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FilenameUtils.getExtension;

import com.iceberg.entity.InvoiceDetail;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.externalapi.ImageStorageService;
import com.iceberg.externalapi.PayPalService;
import com.iceberg.externalapi.impl.ProcessingForRequestImpl;
import com.iceberg.service.ReiRequestService;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.Config;
import com.iceberg.utils.MailUtils;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import com.iceberg.utils.Utils;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/reirequest")
public class ReiRequestController {

  private static final Set<String> supportedImageExt = new HashSet<>();

  static {
    supportedImageExt.add("png");
    supportedImageExt.add("jpg");
    supportedImageExt.add("jpeg");
    supportedImageExt.add("gif");
  }

  private Logger logger = LoggerFactory.getLogger(ReiRequestController.class);
  @Resource
  private ReiRequestService reiRequestService;
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private PayPalService payPalService;
  @Resource
  private ImageStorageService imageStorageService;
  @Resource
  private ProcessingForRequestImpl ocrService;

  /**
   * add reimbursement request.
   *
   * @param reimbursementRequest reimbursment request message
   * @param session http session
   * @return result message
   */
  @RequestMapping(value = "/addRequest", method = RequestMethod.POST)
  public Result add(ReimbursementRequest reimbursementRequest, HttpSession session) {
    reimbursementRequest.setUserid(Config.getSessionUser(session).getId());

    UserInfo requestUserInfo = (UserInfo) session.getAttribute(Config.CURRENT_USERNAME);
    Utils.log(reimbursementRequest.toString());
    // set default pay way = paypal
    if (reimbursementRequest.getPaywayid() == null || reimbursementRequest.getPaywayid() <= 0) {
      reimbursementRequest.setPaywayid(1);
    }
    try {
      String email = requestUserInfo.getEmail();
      MailUtils.sendMail(email, MailUtils.posted);
      reimbursementRequest.setTypeid(PROCESSING.value);
      logger.debug(reimbursementRequest.toString());
      int num = reiRequestService.add(reimbursementRequest);
      if (num > 0) {
        return ResultUtil.success("Reimbursement Request Successfully!", reimbursementRequest);
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * the owen of the reimbursement request can update the reimbursement.
   *
   * @param reimbursementRequest reimbursement request
   * @param session http session
   * @return result information
   */
  @RequestMapping(value = "/updateRequest", method = RequestMethod.POST)
  public Result update(ReimbursementRequest reimbursementRequest, HttpSession session) {
    Utils.log(reimbursementRequest.toString());
    int userId = Config.getSessionUser(session).getId();
    int roleId = Config.getSessionUser(session).getRoleid();
    ReimbursementRequest result = reiRequestService
        .getReimRequestById(reimbursementRequest.getId());
    if (result == null) {
      return ResultUtil.unSuccess("Such reimbursement request doesn't exist!");
    } else {
      if (roleId == 3 && result.getUserid() != userId) {
        return ResultUtil.unSuccess("Permission denied!");
      } else if (result.getTypeid() == APPROVED) {
        return ResultUtil.unSuccess("Request has already been approved!");
      }
      try {
        if (reimbursementRequest.getPaywayid() == -1) {
          reimbursementRequest.setPaywayid(0);
        }
        reimbursementRequest.setTypeid(PROCESSING.value);
        int num = reiRequestService.update(reimbursementRequest);
        if (num > 0) {
          return ResultUtil.success("Update successfully!", null);
        } else {
          return ResultUtil.unSuccess();
        }
      } catch (Exception e) {
        return ResultUtil.error(e);
      }
    }
  }

  /**
   * review reimbursement request only for admin and group manager.
   *
   * @param reimbursementRequest reimbursement request
   * @param session http session
   * @return result information whether the request has been approved.
   */
  @RequestMapping(value = "/review/{typeid}/{userid}/{reimId}/{comments}",
      method = RequestMethod.POST)
  public Result review(ReimbursementRequest reimbursementRequest, HttpSession session,
      @PathVariable String typeid, @PathVariable String userid, @PathVariable String reimId,
      @PathVariable String comments)
      throws IOException {
    if (Config.getSessionUser(session).getRoleid() > 2) {
      return ResultUtil.unSuccess("Permission denied. Don't have review access.");
    }
    System.out.println("type id :" + typeid + "userid : " + userid + "reimId : " + reimId);
    reimbursementRequest.setTypeid(Integer.parseInt(typeid));
    UserInfo requestUserInfo = userInfoService.getUserInfoById(userid);
    System.out.println("requestUserInfo is null ? " + requestUserInfo);
    ReimbursementRequest request = reiRequestService.getReimRequestById(Integer.parseInt(reimId));
    request.setTypeid(Integer.parseInt(typeid));
    request.setComments(comments);
    if (reimbursementRequest.getTypeid() == APPROVED) {
      // email send service
      sendEmailHelper(requestUserInfo, MailUtils.approved);
      // paypal send service
      try {
        String receiver = request.getReceiveraccount();
        Float money = request.getMoney();
        if (receiver == null || receiver.equals("")) {
          return ResultUtil.unSuccess("No receiver account");
        }
        if (money == null) {
          return ResultUtil.unSuccess("Please input money");
        }
        payPalService.createPayout(receiver, "USD", money.toString());
        //  return ResultUtil.success("Approved");
      } catch (Exception e) {
        e.printStackTrace();
      }

    } else if (reimbursementRequest.getTypeid() == PROCESSING) {
      return ResultUtil.unSuccess("Not a valid review");
    } else if (reimbursementRequest.getTypeid() == DENIED) {
      // send denied email
      sendEmailHelper(requestUserInfo, MailUtils.denied);
    }
    try {
      int num = reiRequestService.update(request);
      if (num > 0) {
        return ResultUtil.success(
            String.format("Update successfully! Status : %s", reimbursementRequest.getTypeid()),
            null);
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * send email helper in rei request controller.
   *
   * @param requestUserInfo request user info
   * @param content email content type.
   */
  private void sendEmailHelper(UserInfo requestUserInfo, String content) {
    try {
      System.out.println("requestUserInfo is null ? " + requestUserInfo);
      String email = requestUserInfo.getEmail();
      MailUtils.sendMail(email, content);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * get reimbursement request by request id.
   *
   * @param id request id.
   * @return result messsage
   */
  @RequestMapping("/getReiRequestById/{id}")
  public Result getReiRequestById(@PathVariable Integer id) {
    ReimbursementRequest reimbursementRequestCondition = new ReimbursementRequest();
    reimbursementRequestCondition.setId(id);
    return reiRequestService.findByWhereNoPage(reimbursementRequestCondition);

  }

  /**
   * get reimbursement request by user id with page no and page size.
   *
   * @param userid user id.
   * @param pageNo page no
   * @param pageSize page size
   * @return result message
   */
  @RequestMapping("/getReiRequestByUserId/{userid}/{pageNo}/{pageSize}")
  public Result getReiRequestByUserId(@PathVariable int userid, @PathVariable int pageNo,
      @PathVariable int pageSize) {
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(userid);

    PageModel model = new PageModel<>(pageNo, reimbursementRequest);
    model.setPageSize(pageSize);

    return reiRequestService.findByWhere(model);
  }

  /**
   * get reimbursement request.
   *
   * @param reimbursementRequest reimbursement request query messsage.
   * @param pageNo page no.
   * @param pageSize page size
   * @param session http session.
   * @return result message.
   */
  @RequestMapping("/getReiRequest/{pageNo}/{pageSize}")
  public Result getReiRequest(ReimbursementRequest reimbursementRequest, @PathVariable int pageNo,
      @PathVariable int pageSize, HttpSession session) {
    System.out.println("reimbursementRequest:" + reimbursementRequest);
    Utils.log(reimbursementRequest.toString());
    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    if (reimbursementRequest.getTitle() != null && !reimbursementRequest.getTitle().equals("")) {
      reimbursementRequestSearch.setTitle(reimbursementRequest.getTitle());
    }
    if (reimbursementRequest.getRemark() != null) {
      reimbursementRequestSearch.setRemark(reimbursementRequest.getRemark());
    }
    if (reimbursementRequest.getTypeid() != null) {
      reimbursementRequestSearch.setTypeid(reimbursementRequest.getTypeid().value);
    }
    if (reimbursementRequest.getPaywayid() != null && reimbursementRequest.getPaywayid() != -1) {
      reimbursementRequestSearch.setPaywayid(reimbursementRequest.getPaywayid());
    }
    if (reimbursementRequest.getRealname() != null) {
      reimbursementRequestSearch.setRealname(reimbursementRequest.getRealname());
    }
    UserInfo currentUser = Config.getSessionUser(session);
    // search for group reimbursement information if group manager
    if (currentUser.getRoleid() == 2) {
      reimbursementRequestSearch.setGroupid(currentUser.getGroupid());
    } else if (currentUser.getRoleid() == 3) {
      reimbursementRequestSearch.setUserid(currentUser.getId());
    }
    PageModel model = new PageModel<>(pageNo, reimbursementRequestSearch);
    model.setPageSize(pageSize);

    return reiRequestService.findByWhere(model);
  }

  /**
   * get request with no page.
   *
   * @param reimbursementRequest eimbursement request query messsage.
   * @param session http session.
   * @return result message.
   */
  @RequestMapping("/getReiRequestByNoPage")
  public Result getReiRequestByNoPage(ReimbursementRequest reimbursementRequest,
      HttpSession session) {
    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();

    UserInfo currentUser = Config.getSessionUser(session);
    // search for group reimbursement information if group manager
    if (currentUser.getRoleid() == 2) {
      reimbursementRequestSearch.setGroupid(currentUser.getGroupid());
    } else if (currentUser.getRoleid() == 3) {
      reimbursementRequestSearch.setUserid(currentUser.getId());
    }
    return reiRequestService.findByWhereNoPage(reimbursementRequestSearch);
  }

  /**
   * delete reimbursement request.
   *
   * @param id request id.
   * @return result message.
   */
  @RequestMapping("/delReiRequest")
  public Result del(int id) {
    try {
      int num = reiRequestService.del(id);
      if (num > 0) {
        return ResultUtil.success("delete successfully!", null);
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * get request image by request id.
   *
   * @param requestId request id
   * @return image.
   */
  @RequestMapping(value = "/{requestId}/image", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageByRequestId(@PathVariable Integer requestId) {
    ReimbursementRequest request = reiRequestService.getReimRequestById(requestId);
    String imageId = request.getImageid();
    return getImageByImageId(imageId);
  }

  /**
   * return image to front-end.
   *
   * @param imageId image name
   * @return response entity with image
   */
  @RequestMapping(value = "/image/{imageId}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageByImageId(@PathVariable String imageId) {
    HttpHeaders headers = new HttpHeaders();
    byte[] data = imageStorageService.getImageBytes(imageId);
    if (data == null) {
      logger.error("No such image in the image storage!");
      return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
    }
    boolean succ = setMediaType(imageId, headers);
    if (!succ) {
      return new ResponseEntity<>(null, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(data, headers, HttpStatus.OK);
    logger.debug("responseEntity: " + responseEntity.toString());
    return responseEntity;
  }

  /**
   * set response media type.
   *
   * @param imageId image id.
   * @param headers http headers.
   * @return if success
   */
  private Boolean setMediaType(String imageId, HttpHeaders headers) {
    try {

      String ext = getExtension(imageId);
      switch (ext.toLowerCase()) {
        case "png":
          headers.setContentType(MediaType.IMAGE_PNG);
          break;
        case "jpg":
        case "jpeg":
          headers.setContentType(MediaType.IMAGE_JPEG);
          break;
        case "gif":
          headers.setContentType(MediaType.IMAGE_GIF);
          break;
        default:
          return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      return false;
    }
    return true;
  }

  /**
   * extract information from uploaded image.
   *
   * @param imageFile image file.
   * @param session http session.
   * @return analysis result
   */
  @RequestMapping(value = "/image/analysis", method = RequestMethod.POST)
  public Result analysisImage(@RequestParam("imageFile") MultipartFile imageFile,
      HttpSession session) {
    ReimbursementRequest requestResult = new ReimbursementRequest();
    Result uploadImageResult = uploadImageToStorage(imageFile);
    if (uploadImageResult.getCode() != 200) {
      return uploadImageResult;
    }
    try {
      String imageId = (String) uploadImageResult.getData();
      requestResult.setImageid(imageId);
      String imageFileBase64 = Base64.getEncoder().encodeToString(imageFile.getBytes());
      InvoiceDetail invoiceDetail = ocrService.parseFromDocumentBase64(imageFileBase64);
      requestResult.setMoney(invoiceDetail.getMoney());
      requestResult.setVendorname(invoiceDetail.getVendorname());
      requestResult.setVendoraddr(invoiceDetail.getVendoraddr());
      requestResult.setDuedate(invoiceDetail.getDuedate());
      //      requestResult.setMoney((float)1.23);
      //      requestResult.setVendorname("Apple");
      //      requestResult.setVendoraddr("NYC");
      //      requestResult.setDuedate("2020-12-01");

      return ResultUtil.success(requestResult);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResultUtil.unSuccess(e.getMessage());
    }
  }

  private Result uploadImageToStorage(MultipartFile imageFile) {
    String imageName;

    imageName = StringUtils.cleanPath(imageFile.getOriginalFilename());
    logger.debug("image name: " + imageName);

    String ext = getExtension(imageName);
    if (!supportedImageExt.contains(ext.toLowerCase())) {
      return ResultUtil.unSuccess("Unsupported image type");
    }
    String imageId = randomUUID().toString() + "." + getExtension(imageName);
    logger.debug("image id: " + imageId);
    try {
      String putImageResponse = imageStorageService.putImage(imageId, imageFile.getBytes());
      if (putImageResponse == null) {
        return ResultUtil.unSuccess("Upload image to image storage fails!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      return ResultUtil.error(e);
    }
    return ResultUtil.success("Upload image to storage successfully!", imageId);
  }

  /**
   * upload image to image storage.
   *
   * @param requestId corresponding request id
   * @param imageFile image file
   * @return upload image result
   */
  @RequestMapping(value = "/{requestId}/image", method = RequestMethod.POST)
  public Result uploadImage(@PathVariable Integer requestId,
      @RequestParam("imageFile") MultipartFile imageFile, HttpSession session) {
    // check whether current user is legal to revise.

    ReimbursementRequest reimbursementRequest = reiRequestService.getReimRequestById(requestId);
    if (reimbursementRequest == null) {
      return ResultUtil.unSuccess("request not found!");
    }
    if (!reimbursementRequest.getUserid().equals(Config.getSessionUser(session).getId())) {
      return ResultUtil.unSuccess("Not authorized to upload image!");
    }
    Result uploadImageResult = uploadImageToStorage(imageFile);
    if (uploadImageResult.getCode() != 200) {
      return uploadImageResult;
    }
    String imageId = (String) uploadImageResult.getData();
    reimbursementRequest.setImageid(imageId);
    try {
      int num = reiRequestService.update(reimbursementRequest);
      if (num > 0) {
        return ResultUtil.success("Upload Image successfully!");
      } else {
        return ResultUtil.unSuccess("Update image id fails!");
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }
}
