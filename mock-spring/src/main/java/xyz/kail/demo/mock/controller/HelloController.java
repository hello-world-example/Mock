package xyz.kail.demo.mock.controller;

import org.springframework.web.bind.annotation.*;
import xyz.kail.demo.mock.model.SayVO;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo/hello")
public class HelloController {

    @GetMapping("/say/{world}")
    public Map<String, Object> index(
            @PathVariable("world") String world,
            @RequestParam(value = "p1", required = false) String p1
    ) {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("world", world);
        result.put("p1", p1);
        return result;
    }

    @PostMapping("/say/{world}")
    public Map<String, Object> index(@PathVariable("world") String world, String p1, @RequestBody SayVO say) {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("world", world);
        result.put("p1", p1);
        result.put("sayVO", say);
        return result;
    }

}
