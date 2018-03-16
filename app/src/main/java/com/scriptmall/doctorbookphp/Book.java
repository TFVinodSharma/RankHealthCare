package com.scriptmall.doctorbookphp;

/**
 * Created by scriptmall on 11/15/2017.
 */

public class Book {

    String lname,lphone,laddr,lepx,lrate,lspl;
    String rname,rrate,rdesc,rdate,rimg,docid,docname,rid,rtitle;
    String bdate,btime,bstatus,bid,catid,review_status,app_status;

    private boolean isSelected;



    public  Book(){}

    public Book(String lname){
        this.lname=lname;
    }

    public Book(String lname,String lphone,String laddr,String lepx,String lrate,String lspl){
        this.lname=lname;
        this.lphone=lphone;
        this.laddr=laddr;
        this.lepx=lepx;
        this.lrate=lrate;
        this.lspl=lspl;

        this.lname=lname;
        this.lphone=lphone;
        this.btime=laddr;
        this.bdate=lepx;
        this.bstatus=lrate;
        this.lspl=lspl;

    }

    public Book(String pname, String pphone, String paddr,String prate){
        this.rname=pname;
        this.rrate=pphone;
        this.rdesc=paddr;
        this.rdate=prate;

        this.lname=pname;
        this.bstatus=pphone;
        this.btime=paddr;
        this.bdate=prate;
    }



    public String getLname() {  return lname;  }
    public void setLname(String lname) {  this.lname = lname;  }

    public String getLphone() {  return lphone;  }
    public void setLphone(String lphone) {  this.lphone = lphone;  }

    public String getLaddr() {  return laddr;  }
    public void setLaddr(String laddr) {  this.laddr = laddr;  }

    public String getLepx() {  return lepx;  }
    public void setLepx(String lepx) {  this.lepx = lepx;  }

    public String getLrate() {  return lrate;  }
    public void setLrate(String lrate) {  this.lrate = lrate;  }

    public String getLspl() {  return lspl;  }
    public void setLspl(String lspl) {  this.lspl = lspl;  }

    public String getRname() {  return rname;  }
    public void setRname(String rname) {  this.rname = rname;  }

    public String getRrate() {  return rrate;  }
    public void setRrate(String rrate) {  this.rrate = rrate;  }

    public String getRtitle() {  return rtitle;  }
    public void setRtitle(String rtitle) {  this.rtitle = rtitle;  }

    public String getRdesc() {  return rdesc;  }
    public void setRdesc(String rdesc) {  this.rdesc = rdesc;  }

    public String getRdate() {  return rdate;  }
    public void setRdate(String rdate) {  this.rdate = rdate;  }

    public String getRimg() {  return rimg;  }
    public void setRimg(String rimg) {  this.rimg = rimg;  }

    public String getBdate() {  return bdate;  }
    public void setBdate(String bdate) {  this.bdate = bdate;  }

    public String getBtime() {  return btime;  }
    public void setBtime(String btime) {  this.btime = btime;  }

    public String getBstatus() {  return bstatus;  }
    public void setBstatus(String bstatus) {  this.bstatus = bstatus;  }

    public String getBid() {  return bid;  }
    public void setBid(String bid) {  this.bid = bid;  }

    public String getDocid() {  return docid;  }
    public void setDocid(String docid) {  this.docid = docid;  }

    public String getDocname() {  return docname;  }
    public void setDocname(String docname) {  this.docname = docname;  }

    public String getRid() {  return rid;  }
    public void setRid(String rid) {  this.rid = rid;  }

    public String getCatid() {  return catid;  }
    public void setCatid(String catid) {  this.catid = catid;  }

    public String getReview_status() {  return review_status;  }
    public void setReview_status(String review_status) {  this.review_status = review_status;  }

    public String getApp_status() {  return app_status;  }
    public void setApp_status(String app_status) {  this.app_status = app_status;  }



    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean isSelected) { this.isSelected = isSelected; }


}
