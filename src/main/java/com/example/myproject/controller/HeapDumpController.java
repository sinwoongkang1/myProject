package com.example.myproject.controller;

import com.example.myproject.config.HeapDumpUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HeapDumpController {
    @GetMapping("/heapdump")
    public String createHeapDump(@RequestParam(defaultValue = "/tmp/heapdump.hprof") String filePath) {
        HeapDumpUtil.dumpHeap(filePath, true);
        return "Heap dump created at " + filePath;
    }
}
