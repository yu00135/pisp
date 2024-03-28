package com.mmy.pisp.result;

import lombok.Getter;

/**
 * @author MaMingyu
 */

@Getter
public enum ResultCode {
//100      continue(继续)                                 收到了请求的起始部分，客户端应该继续请求
//
//101      switching protocols(切换协议)                   服务器正根据客户端的指示将协议切换成update首部列出的协议
//
//
//
//200      ok                                             服务器已经成功处理请求
//
//201      created(已经建立)                               对那些要服务器创建对象的请求来说，资源已创建完毕
//
//202      accepted(已接受)                                请求已接受，但服务器尚未处理
//
//203      non-authoritative-information(非权威信息)       服务器已将事务成功处理，只是实体首部包含的信息不是来自原始服务器，而是来自资源的副本
//
//204      no content(没有内容)                            响应报文包含一些首部和一个状态行，但不包含实体的主体内容
//
//205      reset content (重置内容)                        另一个主要用于浏览器的代码。意思是浏览器应该重置当前页面上所有的HTML表单
//
//206      partial content(部分内容)                       部分请求成功
//
//
//
//300      multiple choices(多项选择)                      客户端请求了实际指向多个资源的URL。这个代码是和一个选项列表一起返回的，
//    然后用户就可以选择他希望使用的选项了。
//
//            301      moved permanently(永久搬离)                     请求的URL已移走。响应中应该包含一个location URL，说明资源现在所处的位置
//
//302      Found(已找到)                                   与状态码301类似，但这里的搬离是临时的，
//    客户端应该用Location首部给出的URL对资源进行临时定位
//
//303      see other                                      告诉客户端应该用另一个URL获取资源。这个新的URL位于响应报文的location首部
//
//304      not modified(未修改)                            客户端可以通过它们所包含的请求首部发起条件请求。说明资源未发生过变化
//
//305      use proxy(使用代理)                             必须通过代理访问资源，代理的位置是在location首部中给出的
//
//306      未使用
//
//307     temporary  redirect(临时重定向)                 和301类似，但客户端应该用location首部给出的URL对资源进行临时定位
//
//
//400     bad request                                    告诉客户端它发送了一条异常请求
//
//401     unauthorized(未授权)                           与适当的首部一起返回，在客户端获得资源访问权之前，请它进行身份认证
//
//402     payment required(要求付款)                     未使用
//
//403     forbidden(禁止)                               服务器拒绝了请求
//
//404     not found(未找到)                             服务器无法找到请求的URL
//
//405     menthod not allowed(不允许使用的方法)          请求中有一个所请求的URL不支持的方法。响应中应该包含一个allow首部，
//    以告知客户端所请求的资源支持使用哪些方法
//
//406    not acceptable(无法接受)                       客户端可以指定一些参数来说明希望接受哪些类型的实体。
//    服务器没有资源与客户端可接受的URL相匹配时可使用此代码
//
//407    proxy authentication required(要求进行代理认证) 和401类似，但用于需要进行资源认证的代理服务器
//
//408    request timeout(请求超时)                      如果客户端完成其请求时花费的时间太长，服务器可以回送这个状态码并关闭连接
//
//409    conflict(冲突)                                 发出的请求在资源上造成一些冲突
//
//410    gone(消失了)                                   除了服务器曾持有这些资源之外，与404类似
//
//411    length required(要求长度指示)                  服务器要求在请求报文中包含content-length首部时会使用这个代码。发起的请求中若没有
//    content-length 首部，服务器是不会接受此资源请求的
//
//412    precondition failed(先决条件失败)              如果客户端发起了一个条件请求，如果服务器无法满足其中的某个条件，就返回这个响应码
//
//413    request entity too large(请求实体太大)         客户端发送的实体主体部分比服务器能够或者希望处理的要大
//
//414    request URL too long(请求URL太长)              客户端发送的请求所携带的请求URL超过了服务器能够或者希望处理的长度
//
//415    unsupported media type(不支持的媒体类型)       服务器无法理解或不支持客户端所发送的实体的内容类型
//
//416    requested range not satisfiable(所请求的范围未得到满足)   请求报文请求的是某个范围内的指定资源，但那个范围无效，或者未得到满足
//
//417    expectation failed(无法满足期望)              请求的expect首部包含一个预期内容，但服务器无法满足
//
//
//500    internal server error(内部服务器错误)         服务器遇到一个错误，使其无法为请求提供服务
//
//501    Not implemented(未实现)                      服务器无法满足客户端请求的某个功能
//
//502    网关故障bad gateway                          作为代理或者网关使用的服务器遇到了来自响应链中上游的无效响应
//
//503    service unavailable未提供此服务              服务器目前无法为请求提供服务，但过一段时间就可以恢复服务
//
//504    gateway timeout 网关超时                    与408类似，但是响应来自网关或者代理，此网关或者代理在等待另一台服务器的响应时出现了超时
//
//505    HTTP version not support(不支持http版本)    服务器收到的请求时以它不支持或不愿支持的协议版本表示的
;
    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
