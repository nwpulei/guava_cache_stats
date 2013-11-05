package com.nwpulei.guava.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;

/**
 * User: leilei Date: 13-11-5 Time: 下午5:31
 */
@Controller
@RequestMapping("/")
public class CacheStats {
    private final Cache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS)
            .build();

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/add")
    public ModelAndView add(@RequestParam String key, @RequestParam String value) {
        cache.put(key, value);
        return new ModelAndView("show", ImmutableMap.of("title", "add k-v", "key", key, "value", value));
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam String key) {
        cache.invalidate(key);
        return new ModelAndView("show", ImmutableMap.of("title", "del k", "key", key));
    }

    @RequestMapping("/get")
    public ModelAndView get(@RequestParam String key) {
        if (cache.getIfPresent(key) == null) {
            return new ModelAndView("message", ImmutableMap.of("title", "message", "message", "can't find value"));
        } else {
            return new ModelAndView("show", ImmutableMap.of("title", "get k-v", "key", key, "value",
                    cache.getIfPresent(key)));
        }
    }

    @RequestMapping("/stats")
    public ModelAndView stats() {
        return new ModelAndView("stats", "stats", cache.stats());
    }
}
