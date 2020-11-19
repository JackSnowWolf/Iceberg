package com.iceberg.utils;

public class PageModel<T> {

  private int beginIndex;
  private int currentPageNo;
  private int pageSize = 20;
  private T data;

  /**
   * create page model.
   * @param currentPageNo current page number.
   * @param data data.
   */
  public PageModel(int currentPageNo, T data) {
    if (currentPageNo < 1) {
      throw new IllegalArgumentException("currentPageNo can not less than one");
    } else {
      this.currentPageNo = currentPageNo;
      this.data = data;
    }
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public void setBeginIndex(int beginIndex) {
    this.beginIndex = beginIndex;
  }

  public int getCurrentPageNo() {
    return currentPageNo;
  }

  public void setCurrentPageNo(int currentPageNo) {
    this.currentPageNo = currentPageNo;
  }

  public int getPageSize() {
    return pageSize;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public int getBeginIndex() {
    return beginIndex = (currentPageNo - 1) * pageSize;
  }

  @Override
  public String toString() {
    return "PageModel{" + "beginIndex=" + beginIndex + ", currentPageNo=" + currentPageNo
        + ", pageSize=" + pageSize + ", data=" + data + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    return this.toString().equals(obj.toString());
  }
}
