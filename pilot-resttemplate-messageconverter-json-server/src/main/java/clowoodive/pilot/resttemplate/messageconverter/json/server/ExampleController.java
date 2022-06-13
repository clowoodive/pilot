package clowoodive.pilot.resttemplate.messageconverter.json.server;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/example", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExampleController {

    @PostMapping(value = "/offsetdatetime")
    public RestProto.Res offsetDateTime(@RequestBody RestProto.Req req) {
        System.out.println("reqId: " + req.getReqId());
        RestProto.Res res = new RestProto.Res();
        res.setResId(req.getReqId());
//        var resAt = OffsetDateTime.now();
//        var resAt = req.getReqAt().plusDays(1)
//        var resAt = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(8));
        var resAt = OffsetDateTime.of(2022, 5, 22, 11, 22, 33, 0, ZoneOffset.ofHours(8));
        res.setResAt(resAt);

        System.out.println("resAt: " + res.getResAt());

        return res;
    }
}
