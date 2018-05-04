package testcode.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SpringUnvalidatedContentTypeController {

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void requestMappingPostNoContentType() { }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public void requestMappingPostWithContentType() { }

    @PostMapping(value = "/")
    public void postMappingNoContentType() { }

    @PostMapping(value = "/", consumes = "application/json")
    public void postMappingWithContentType() { }
}
