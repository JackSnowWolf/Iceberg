package com.iceberg.utils;

import java.util.List;

/**
 *
 */
public class ResultUtil {

  public static Result success() {
    Result result = new Result();
    result.setCode(Config.SUCCESS);
    result.setMsg("Operation successful!");
    return result;
  }

  public static Result success(List list) {
    Result result = new Result();
    result.setCode(Config.SUCCESS);
    result.setMsg("Operation successful!");
    result.setDatas(list);
    return result;
  }

  public static Result success(Object o) {
    Result<Object> result = new Result<>();
    result.setCode(Config.SUCCESS);
    result.setMsg("Operation successful!");
    result.setData(o);
    return result;
  }

  public static Result success(String msg, Object object) {
    Result result = new Result();
    result.setCode(Config.SUCCESS);
    result.setMsg(msg);
    result.setData(object);
    return result;
  }

  public static Result error(Exception e) {
    Result result = new Result();
    result.setCode(Config.ERROR);
    result.setMsg("Operation failed, causing exceptions");
    Utils.log(e.getMessage());
    return result;
  }

  public static Result unSuccess() {
    Result result = new Result();
    result.setCode(Config.UNSUCCESS);
    result.setMsg("Operation failed!");
    return result;
  }

  public static Result unSuccess(String msg) {
    Result result = new Result();
    result.setCode(Config.UNSUCCESS);
    result.setMsg(msg);
    return result;
  }
}
