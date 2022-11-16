package clowoodive.pilot.spring.filterregistration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

    @GetMapping("/filter/message")
    public String createMessageForm(Model model) {
        return "message-form";
    }

    @PostMapping("/filter/message")
    public String createMessage(MessageForm form) {
        System.out.println("msg :" + form.getMessage());
        return "redirect:/filter/message";
    }
}
