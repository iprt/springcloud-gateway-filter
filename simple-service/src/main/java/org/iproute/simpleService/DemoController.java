package org.iproute.simpleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * DemoController
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@RestController
@Slf4j
public class DemoController {

    @GetMapping("/empty")
    public void empty() {
        log.info("empty invoked ...");
    }

    @GetMapping("/getInt")
    public int getInt() {
        log.info("get int invoked ...");
        return 123;
    }

    @GetMapping("/getDouble")
    public double getDouble() {
        log.info("get double invoked ...");
        return 123.345;
    }

    @GetMapping("/getStr")
    public String getStr() {
        log.info("get string ...");
        return "Simple Service";
    }

    @PostMapping("/getMap")
    public Map<String, Object> getMap(@RequestBody Map<String, Object> map) {
        log.info("get Map invoked ; map = {}", map);
        return map;
    }

}
