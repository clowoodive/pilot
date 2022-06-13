package clowoodive.pilot.resttemplate.messageconverter.json.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution)
            throws IOException {

        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        return traceResponse(request, body, response);
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[request : ").append(request.getMethod()).append(" ").append(request.getURI()).append("\n");
        sb.append("headers : ").append(request.getHeaders()).append("\n");
        if (body != null && body.length > 0) {
            sb.append("body : ").append(new String(body, StandardCharsets.UTF_8)).append("\n");
        }
        sb.append("]");
        log.debug(sb.toString());
    }

    private ClientHttpResponse traceResponse(final HttpRequest request, final byte[] body, final ClientHttpResponse response) {
        final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);

        try {
            var statusCode = responseCopy.getStatusCode();

            StringBuilder bodyBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseCopy.getBody(), StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            while (line != null) {
                bodyBuilder.append(line);
                bodyBuilder.append('\n');
                line = bufferedReader.readLine();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("[response : ").append(statusCode.value()).append(" ").append(statusCode.getReasonPhrase()).append("\n");
            // sb.append("StatusText : ").append(responseCopy.getStatusText()).append("\n");
            sb.append("headers : ").append(responseCopy.getHeaders()).append("\n");
            sb.append("body : ").append(bodyBuilder.toString()).append("\n");
            sb.append("]");
            log.debug(sb.toString());

        } catch (Exception ex) {
            log.warn("traceResponse", ex);
            return responseCopy;
        }

        return responseCopy;
    }
}

final class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse response;

    private byte[] body;

    BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
        this.response = response;
    }

    public HttpStatus getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    public int getRawStatusCode() throws IOException {
        return this.response.getRawStatusCode();
    }

    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    public InputStream getBody() throws IOException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(this.response.getBody());
        }
        return new ByteArrayInputStream(this.body);
    }

    public void close() {
        this.response.close();
    }
}
