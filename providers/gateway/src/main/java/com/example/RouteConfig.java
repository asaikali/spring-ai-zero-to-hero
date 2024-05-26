package com.example;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.BodyFilterFunctions.adaptCachedBody;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import com.example.log.AuditLogEntry;
import com.example.log.OpenAiAuditor;
import com.example.log.RequestLogEntry;
import com.example.log.ResponseLogEntry;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class RouteConfig {
  /**
   * uri: https://api.openai.com/v1/chat/completions predicates: - Path=/openai/** filters: -
   * StripPrefix=1
   *
   * @return
   */
  // http POST :8080/post hello=world
  @Bean
  public RouterFunction<ServerResponse> readBodyRoute(
      HttpServletRequest httpServletRequest, OpenAiAuditor openAiAuditor) {
    return route("openai")
        .route(path("/openai/**"), http("https://api.openai.com/v1/chat/completions"))
        .route(path("/ollama/**"), http("http://localhost:11434/"))
        .before(stripPrefix(1))
        .before(
            request -> {
              var requestLogEntry = new RequestLogEntry();

              // log the body
              Optional<String> body = MvcUtils.cacheAndReadBody(request, String.class);
              requestLogEntry.setBody(body.orElse(""));

              // log the headers
              Map<String, String> headers = request.headers().asHttpHeaders().toSingleValueMap();
              requestLogEntry.setHeaders(headers);

              // log the http method and incoming request uri
              requestLogEntry.setMethod(request.method().name());
              requestLogEntry.setOriginalUri(request.uri().toString());

              // put audit log entry in the request
              var auditLogEntry = new AuditLogEntry();
              auditLogEntry.setRequest(requestLogEntry);
              MvcUtils.putAttribute(request, AuditLogEntry.AUDIT_LOG_ENTRY, auditLogEntry);

              return request;
            })
        .before(adaptCachedBody()) // make the body readable for next step
        .after(
            (request, response) -> {
              var responseLogEntry = new ResponseLogEntry();

              // log the response headers
              Map<String, String> headers = response.headers().toSingleValueMap();
              responseLogEntry.setHeaders(headers);

              // log the response body
              Object o = request.attributes().get(MvcUtils.CLIENT_RESPONSE_INPUT_STREAM_ATTR);
              if (o instanceof InputStream) {
                try {
                  byte[] bytes = StreamUtils.copyToByteArray((InputStream) o);
                  String body = new String(bytes, StandardCharsets.UTF_8);
                  responseLogEntry.setBody(body);
                  ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
                  request.attributes().put(MvcUtils.CLIENT_RESPONSE_INPUT_STREAM_ATTR, bais);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }

              // add the response to audit entry
              AuditLogEntry auditLogEntry =
                  MvcUtils.getAttribute(request, AuditLogEntry.AUDIT_LOG_ENTRY);
              auditLogEntry.setResponse(responseLogEntry);

              // log the uri where the request was sent to
              URI uri = MvcUtils.getAttribute(request, MvcUtils.GATEWAY_REQUEST_URL_ATTR);
              auditLogEntry.getRequest().setDestinationUri(uri.toString());

              openAiAuditor.log(auditLogEntry);

              return response;
            })
        .build();
  }
}
