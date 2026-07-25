package com.springboot.web;

import com.springboot.service.DataService;
import com.springboot.service.XmlFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/web")
public class UserWebController {
    @Autowired
    private DataService service;

    @GetMapping("/list")
    public String viewList(@RequestParam(required = false) String keyword, Model model) {
        List<ServerStatus> servers = service.findAll();

        if (keyword == null || keyword.equals("*")) {
            model.addAttribute("servers",servers);
        }else{
            List<ServerStatus> searchResutl = servers.stream()
                    .filter(s -> s.getName().equals(keyword))
                    .toList();
            model.addAttribute("servers", searchResutl);
            model.addAttribute("keyword", keyword);
        }

        return "list";
    }

    @PostMapping("/release/{id}")
    public String release(@PathVariable String id, Model model) {
        model.addAttribute("message", "Release request for " + id + " has been sent to MQ");
        model.addAttribute("keyword", "");

        return "list";
    }

    @PostMapping("/releaseSelected")
    public String releaseSelected(
            @RequestParam(name = "selectedIds", required = false)
            List<Long> selectedIds,  Model model){

        model.addAttribute("keyword", "");

        if (selectedIds == null || selectedIds.isEmpty()) {
            model.addAttribute("message", "Nothing to Release ");
        }else{
            String idString =String.join(", ", selectedIds.stream().map(String::valueOf).toList());
            model.addAttribute( "message",
                    selectedIds.size() + " ids " + idString + " have been sent to MQ for release");
        }
        return "list";


    }


//    ***********************************************************//
    @GetMapping("/serverstatus")
    public String home(Model model) {

        model.addAttribute("servers",
                service.findAll());

        return "servers";
    }

    @GetMapping("/server/{id}")
    public String details(@PathVariable Long id,
                          Model model) {
        System.out.println("request details for " + id);
        ServerStatus server = service.findById(id);
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<server>" +
                "    <id>" + server.getId() + "</id>" +
                "    <name>" + server.getName() + "</name>" +
                "    <status>" + server.getStatus() + "</status>" +
                "    <cpu>" + server.getCpu() + "</cpu>" +
                "    <memory>" + server.getMemory() + "</memory>" +
                "    <location>" + server.getLocation() + "</location>" +
                "</server>";

        String formattedXml = XmlFormatter.prettyPrint(xmlString);
        server.setXmlStr(formattedXml);
        model.addAttribute("server", server);
        return "detail";
//        return "fragments :: details";
    }

    @PostMapping("/servertoggle/{id}")
    public String toggle(@PathVariable Long id,
                         Model model) {
        System.out.println("Post to toggle " + id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ServerStatus> servers = service.findAll();

        ServerStatus server = servers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (server != null) {
            if ("Running".equals(server.getStatus()))
                server.setStatus("Stopped");
            else
                server.setStatus("Running");

        }

        model.addAttribute("servers", servers);
        System.out.println("return to  fragments :: table");
        return "fragments :: table";
    }
}
