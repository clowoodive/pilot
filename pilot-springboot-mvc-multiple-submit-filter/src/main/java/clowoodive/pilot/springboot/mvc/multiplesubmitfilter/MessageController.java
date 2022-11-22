package clowoodive.pilot.springboot.mvc.multiplesubmitfilter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/filter/message";
    }

    @GetMapping("/filter/message")
    public String createMessageForm() {
        return "message-form";
    }

    @PostMapping("/filter/message")
    @ResponseBody
    public String createMessage(MessageForm form) {
        System.out.println("msg :" + form.getMessage());
        return form.getMessage();
//        return "redirect:/filter/message";
    }
}
