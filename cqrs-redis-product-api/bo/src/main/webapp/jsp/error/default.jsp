<%@ page language="java" contentType="text/html;charset=UTF-8"  pageEncoding="utf-8"  isErrorPage="true" %>

<%@ include file="/WEB-INF/views/layout/common/common.jsp" %>

<%
	boolean isNullException = exception == null;
	boolean isNullErrData = pageContext.getErrorData() == null;
	
	String errmsg = "";
	if (isNullErrData == false)
		errmsg += "[ErrURL="+pageContext.getErrorData().getRequestURI()+"] [Referer="+request.getHeader("referer")+"] [EC:"+pageContext.getErrorData().getStatusCode()+"] ";
	if (isNullException== false)
		errmsg += "[errmsg="+exception.getMessage()+" at "+exception.getStackTrace()[0]+"]";

	if (request.getHeader("referer") != null && request.getHeader("referer").indexOf("uploadify") >= 0) {
		// bypass
		return;
	} 
	else {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="ko" xml:lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>이커머스테크 - 에러페이지</title>

<link rel="shortcut icon" href="/html/images/common/favicon.ico" type="image/x-icon">
<link rel="icon" href="/html/images/common/favicon.ico" type="image/x-icon">
<meta name="robots" content="ALL" />
<meta name="keywords" content="이커머스테크, 에러, 페이지없음" />
<meta name="subject" content="이커머스테크, 페이지 없음" />
<meta name="description" content="페이지를 찾을수 없습니다." />
<meta name="author" content="ectech.co.kr" />
<meta name="copyright" content="ectech.co.kr" />
<meta name="writer" content="이커머스테크" />
<meta name="language" content="ko" />
<meta name="build" content="2014. 11. 12" />	
<style type="text/css">
	.errorWrap{ width:896px;height:197px; margin:0 auto; }
	.errorWrap .contents{position:relative;padding:20px;border:solid 1px #ebebeb;background-color:#fdfdfd;}
	.errorWrap .contents h1{margin:0;padding:0;height:25px;font-size:12px;font-family:dotum;color:#000}
	.errorWrap .contents p{margin:0;padding:0;color:#333;font-size:12px;font-family:dotum;line-height:130%}
	.errorWrap .contents p.help{position:absolute;right:0;top:19px;width:121px;height:39px;border-left:solid 1px #e5e5e5;padding:18px 0px 0px 15px;}
	.errorWrap .contents p.help a{display:block;width:83px;height:18px;padding:4px 0px 0px 11px;border:solid 1px #d5d5d5;text-decoration:none;color:#333}
	.errorWrap .contents p.help a:hover{color:#c40452}
	.errorWrap .btn{padding-top:30px;text-align:center;}
	.errorWrap .btn a{display:inline-block;width:176px;height:22px;padding-top:10px;text-align:center;font-size:12px;font-family:dotum;text-decoration:none;border:solid 1px #d5d5d5;color:#333}
	.errorWrap .btn a.home{background-color:#c40452;border:solid 1px #c40452;color:#fff}
	html, body {
    	height: 100%;
    	width: 100%;
    	overflow:hidden;
	}

</style>
</head>
<body>

<table border="0" style="height:100%; width:100%"><tr><td>
	<div class="errorWrap">
		<div class="contents">
			<h1>이용에 불편을 드려 죄송합니다.</h1>
			<p>
			<%
				if(pageContext.getErrorData().getStatusCode() == 404)
				{
					out.println( "페이지가 존재하지 않습니다." );
				} else {
				 	if (isNullErrData == false)
				 	{
				 		out.println("Error URL : " + pageContext.getErrorData().getRequestURI() + "<br/>"
				 	 			+"Error Code : "+pageContext.getErrorData().getStatusCode() +"<br/>"
				 	 			+"Error Msg : "+exception.getMessage() +"<br/>"
								+"Servlet Name : "+pageContext.getErrorData().getServletName() +"<br/>");
				 	}

					if (isNullException == false)
					{
						out.println("<!--");
						exception.printStackTrace(new java.io.PrintWriter(out));
						out.println("-->");
					}
				}
			%>
			</p>
		</div>
		<div class="btn">
			<a href="#back" onclick="history.back(-1);return false;">이전 페이지로 돌아가기 </a>
			<a href="/" class="home">홈페이지로 이동하기</a>
		</div>
	</div>
</td></tr></table>



</body>
</html>
