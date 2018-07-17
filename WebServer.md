#WebServer流程简介

###先新建一个WebServer的主main类，里面定义ServerSocket和ExecutorService.
1. socket的端口号是8080
2. 线程池是40条

###建一个ClientHandler类，里面定义Socket
1. 用于连接客户端
2. 连接response和request,用request得到url，如果得到的url里面有业务（注册登录表等），就利用反射到向对应的业务类进行处理，如果只是一个简单网页，就在客户端显示页面，否则显示404页面找不到
3. 用response响应客户端
4. 注意一定要包含异常，要不然崩了就完了

###连接request类，里面定义socket和输入流
1. http请求里面包含请求行，请求头和请求正文，请求行里面有三个部分，分别是方法，地址和协议版本
2. method包含post和get（这里介绍流程，不详细介绍这些），地址就是url，你输入在地址栏的东西，些一有1.1和1.0两个版本
3. 正是开始解析，通过socket和ClientHandler连接，再通过输入流里流到ClientHandler中,流读取的内容用StringBuilder进行拼接，以CRLF结尾为一句话
4. 解析请求行，得到第一句话，按照空格分解成三段，假如解析出空请求，就是少于三段，抛出异常，否则进一步解析url，这里可能会出现中文，所以必须先用URLDecoder进行解析，避免乱码，然后按照`？`分解成两段，如果得到的段的长度大于1，则得到`段[1]`的内容并继续根据`&`解析,如果继续解析到两段，就把两段的内容放入parameters中，否则，第二段传入空值。如果没有`?`得到的url直接传到ClientHandler中
5. 解析消息头，用`： `分段，把分出的两段按照键值对的象是放入header这个map中。
6. 解析消息正文，若消息头中包含`Content-Length`，得到`Content-Length`包含的内容，然后读取，并一起解析`Content_Type`,判断后面跟着的内容是否是`application/x-www-form-urlencoded`,如果是，就是有表单，把表单里的内容用ISO8859-1包装，然后用URLDecoder解码，得到的那内容，继续先用`&`解析，然后用`=`解析.
7. 返回给定的名字获得的参数（用于存入的表格里的值或者其他值）


###连接response类，包含Socket和输出流，和一些状态码
1. 请求接受处理后，要响应回客户端，分为三部分，状态行，响应头，响应正文
2. 先响应状态行，发送http版本，状态码，状态，并用CRLF结尾。
3. 发送响应头，遍历消息头中的header，用键值对的方式发送，然后以FRLF结尾。
4. 发送响应正文，文件用流的形式发送出去，用正则表达式自动匹配文件的后缀名，并根据后缀名得到Httpcontext类里xml里的对应标签

###注册，先继承一个HttpServlet父类，用于把文件发送出去
1. 用request的到注册的东西，然后用RandomAccessFile 记录信息，并且响应登录成功的页面

###登录 先继承一个HttpServlet父类，用于把文件发送出去
1. 得到登录的信息，用RandomAccessFile读取信息并且和原来存的比对，比对成功，跳转登录成功页面，比对失败，跳转登录失败页面

###映射页面，在ClientHandler页面，的到的url需要到这里来比对，能够比对成功，就可以映射

###EmptyRequestException类，自己写的异常，见名知义
