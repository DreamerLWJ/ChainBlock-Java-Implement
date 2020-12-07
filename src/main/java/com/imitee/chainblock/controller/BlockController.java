package com.imitee.chainblock.controller;

import com.imitee.chainblock.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: TODO(请添加描述)
 * @Author 11740
 * @Date 2020/12/4 0:31
 */

@Controller
@RequestMapping("block/controller")
public class BlockController {
    private final BlockService blockService;

    public BlockController(@Autowired BlockService blockService) {
        this.blockService = blockService;
    }


    public void launch() {

    }
}
