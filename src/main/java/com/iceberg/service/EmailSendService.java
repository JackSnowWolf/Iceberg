package com.iceberg.service;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;

public interface EmailSendService {

  String postConfirm(UserInfo userInfo, ReimbursementRequest reimbursementRequest);

  String approveConfirm(UserInfo userInfo, ReimbursementRequest reimbursementRequest);

  String denyConfirm(UserInfo userInfo, ReimbursementRequest reimbursementRequest);
}
