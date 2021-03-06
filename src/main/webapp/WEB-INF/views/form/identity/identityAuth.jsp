<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<head>
	 <!-- Stylesheet -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/animate.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/css-plugin-collections.css" rel="stylesheet" />
    <!-- CSS | menuzord megamenu skins -->
    <link id="menuzord-menu-skins"
        href="${pageContext.request.contextPath}/resources/css/menuzord-skins/menuzord-rounded-boxed.css"
        rel="stylesheet" />
    <!-- CSS | Main style file -->
    <link href="${pageContext.request.contextPath}/resources/css/style-main.css" rel="stylesheet" type="text/css">
    <!-- CSS | Preloader Styles -->
    <link href="${pageContext.request.contextPath}/resources/css/preloader.css" rel="stylesheet" type="text/css">
    <!-- CSS | Custom Margin Padding Collection -->
    <link href="${pageContext.request.contextPath}/resources/css/custom-bootstrap-margin-padding.css" rel="stylesheet"
        type="text/css">
    <!-- CSS | Responsive media queries -->
    <link href="${pageContext.request.contextPath}/resources/css/responsive.css" rel="stylesheet" type="text/css">
    <!-- CSS | Style css. This is the file where you can place your own custom css code. Just uncomment it and use it. -->
    <!-- <link href="css/style.css" rel="stylesheet" type="text/css"> -->

    <!-- CSS | Theme Color -->
    <link href="${pageContext.request.contextPath}/resources/css/colors/theme-skin-orange.css" rel="stylesheet"
        type="text/css">

    <!-- external javascripts -->
    <script src="${pageContext.request.contextPath}/resources/js/jquery-2.2.4.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/chart.js"></script>
    <!-- JS | jquery plugin collection for this theme -->
    <script src="${pageContext.request.contextPath}/resources/js/jquery-plugin-collection.js"></script>
    
    
    <script type="text/javascript">
    $(function() {
		//alert(1)
		$("#Authenticate").click(function() {
			//alert(1)
		    if ($("#birthday").val() == "") {
            alert("??????????????? ??????????????????");
            $("#birthday").focus();
            return false;
         };
         
         var now = new Date();
         var year= now.getFullYear();
         //alert(year);
		 //alert(year-($("#birthday").val().substr(0,4))+1);
		 var birth = year-($("#birthday").val().substr(0,4))+1
		 //alert(birth);
           if(birth >= 20) {
        	   alert("?????? ?????? ??????");
        	   var result = true;                 // ?????? ???????????? ???
        	   //opener.parent.completeCallback(result);
        	   opener.document.getElementById("hidden").value = result
        	   opener.document.getElementById("Authenticate").value = "???????????? ??????";
        	    // ?????? ??? ??? ?????? ?????? ??? ?????? ?????????
        	    close();
        	   
           }else{
        	   alert("?????? ????????? ???????????? ??????????????? ??? ??? ????????????.")
        	   var result = false;                 // ?????? ???????????? ???
        	   var theURL = "${pageContext.request.contextPath}/join/"+result; // ?????? URL
        	    // ?????? ??? ?????? ??????????????? URL ??????
        	    opener.window.location = theURL;
        	    // ?????? ??? ??? ?????? ?????? ??? ?????? ?????????
        	    close();
           }
		}) 
	})
    
    </script>
</head>

<body>

<div class="form-group col-md-6">
	<br><br><br>
	<span class="text-theme-colored font-weight-700"><h3>?????? ??????????????? ??????????????????.</h3> </span>
	<input type="text" placeholder="ex) 19960716" id="birthday" class="form-control" maxlength="8"><br>
	<input type="button" value="????????????" name="Authenticate" id="Authenticate"  class="form-control" onclick="completeCallback()">
</div>



</body>