package dev.jlarsen.goldboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class WebController {

    @Autowired
    GoldBoardService goldBoardService;

    @GetMapping(value = "/")
    public String displayBoard(Model model) {
        CurrentProperties properties = goldBoardService.readPropertiesFile();

        if (goldBoardService.checkApiStatus(properties)) {
            model.addAttribute("gold", goldBoardService.getCurrentPrice(properties, Metal.GOLD));
            model.addAttribute("silver", goldBoardService.getCurrentPrice(properties, Metal.SILVER));
            model.addAttribute("platinum", goldBoardService.getCurrentPrice(properties, Metal.PLATINUM));
        } else {
            model.addAttribute("gold", goldBoardService.getBackupPrice(properties, Metal.GOLD));
            model.addAttribute("silver", goldBoardService.getBackupPrice(properties, Metal.SILVER));
            model.addAttribute("platinum", goldBoardService.getBackupPrice(properties, Metal.PLATINUM));
        }

        return "index";
    }
}
