<%--
MIT License

Copyright (c) 2017 Raphael Waller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fn" %>
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <title>Self Service Reset</title>
</head>
<body>
<h1>${authstatus}</h1>
<form action="/inputDetails.do" method="post">
    <h1>Hello and welcome to the Self Service Phone Password Reset</h1>
    <p>
        <label for="number">What's your number?</label>
        <input id="number" name="number" value="${param.number}">
    </p>
    <p>
        <label for="mail">What's your email?</label>
        <input id="mail" name="mailaddress" value="${param.mail}">
    </p>
        <input type="submit">
    </p>
</form>

</body>
</html>