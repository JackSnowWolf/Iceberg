package com.iceberg.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PageModelTest {
    @Test
    public void equalAndSupportTest() {
        PageModel pageModel=new PageModel(1,1);
        PageModel pageModel1=new PageModel(2,2);
        pageModel.setBeginIndex(1);
        int beginIndex=pageModel.getBeginIndex();
        assertNotNull(beginIndex);
        pageModel.setCurrentPageNo(1);
        int currentPageNo=pageModel.getCurrentPageNo();
        assertNotNull(currentPageNo);
        pageModel.setData(2);
        assertFalse(pageModel.equals(pageModel1));
        assertTrue(pageModel.equals(pageModel));
        assertFalse(pageModel.equals(1));
        try{
            PageModel pageModel2=new PageModel(0,1);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
