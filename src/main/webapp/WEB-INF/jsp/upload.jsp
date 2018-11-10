<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018\11\2 0002
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图片上传</title>
</head>
<body>

<form action="" method="post" enctype="multipart/form-data"><%--//enctype进行表单提交时，以二进制提交--%>
    <input type="file" name="upload_file">
    <input type="submit" value="图片上传">

</form>

</body>
</html>
