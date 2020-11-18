package com.iceberg.utils;

import java.util.List;

/**
 * ResultUtil is helper function to deliver result message.
 */
public class ResultUtil {

  /**
   * success result.
   *
   * @return success result.
   */
  public static Result success() {
    Result result = new Result();
    result.setCode(Config.SUCCESS);
    result.setMsg("Operation successful!");
    return result;
  }

  /**
   * success result.
   *
   * @param list data list
   * @return success result
   */
  @SuppressWarnings("unchecked")
  public static Result success(List list) {
    Result result = new Result();
    result.setCode(Config.SUCCESS);
    result.setMsg("Operation successful!");
    result.setDatas(list);
    return result;
  }

  /**
   * success result.
   *
   * @param o object
   * @return success result
   */
  public static Result success(Object o) {
    Result<Object> result = new Result<>();
    result.setCode(Config.SUCCESS);
    result.setMsg("Operation successful!");
    result.setData(o);
    return result;
  }

  /**
   * success result.
   *
   * @param msg success message
   * @param object data object
   * @return success result
   */
  public static Result success(String msg, Object object) {
    Result<Object> result = new Result<>();
    result.setCode(Config.SUCCESS);
    result.setMsg(msg);
    result.setData(object);
    return result;
  }

  /**
   * error result.
   *
   * @param e exception
   * @return error result
   */
  public static Result error(Exception e) {
    Result result = new Result();
    result.setCode(Config.ERROR);
    result.setMsg("Operation failed, causing exceptions");
    Utils.log(e.getMessage());
    return result;
  }

  /**
   * unsuccess result.
   *
   * @return unsuccess result
   */
  public static Result unSuccess() {
    Result result = new Result();
    result.setCode(Config.UNSUCCESS);
    result.setMsg("Operation failed!");
    return result;
  }

  /**
   * unsuccess result.
   *
   * @param msg unsuccess message
   * @return unsuccess result
   */
  public static Result unSuccess(String msg) {
    Result result = new Result();
    result.setCode(Config.UNSUCCESS);
    result.setMsg(msg);
    return result;
  }
}
