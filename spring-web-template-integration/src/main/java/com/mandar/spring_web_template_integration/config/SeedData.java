package com.mandar.spring_web_template_integration.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Authority;
import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.services.AuthorityService;
import com.mandar.spring_web_template_integration.services.PostService;
import com.mandar.spring_web_template_integration.util.constants.Privilege;
import com.mandar.spring_web_template_integration.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        // takes the values from enum Privileges and puts them into Authority model
        // which is then stored in db
        for (Privilege privilege : Privilege.values()) {
            Authority authority = new Authority();
            authority.setId(privilege.getId());
            authority.setName(privilege.getPrivilegeString());
            authorityService.save(authority);
        }

        Set<Authority> all_authorities = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(all_authorities::add);
        authorityService.findById(Privilege.RESET_ANY_USER_PASSWORD.getId()).ifPresent(all_authorities::add);

        Set<Authority> adminPanelAuthority = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(adminPanelAuthority::add);

        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        account01.setEmail("admin@iprocure.com");
        account01.setPassword("pass787");
        account01.setFirstname("admin");
        account01.setLastname("admin");
        account01.setRole(Roles.ADMIN.getRole());
        account01.setAuthorities(adminPanelAuthority);

        account02.setEmail("editor@iprocure.com");
        account02.setPassword("pass787");
        account02.setFirstname("editor");
        account02.setLastname("editor");
        account02.setRole(Roles.EDITOR.getRole());

        account03.setEmail("user@iprocure.com");
        account03.setPassword("pass787");
        account03.setFirstname("user");
        account03.setLastname("user");

        account04.setEmail("super_editor@iprocure.com");
        account04.setPassword("pass787");
        account04.setFirstname("super_editor");
        account04.setLastname("super_editor");
        account04.setRole(Roles.EDITOR.getRole());
        account04.setAuthorities(all_authorities);

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);

        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {

            Post post01 = new Post();
            post01.setTitle("Rockwell Automation");
            post01.setBody("Supplier of industrial automation and information technology");
            post01.setAccount(account01);

            Post post02 = new Post();
            post02.setTitle("Oracle");
            post02.setBody("Supplier of database software and cloud engineered systems");
            post02.setAccount(account02);

            Post post03 = new Post();
            post03.setTitle("Qualcomm");
            post03.setBody("Supplier of mobile processors and wireless technology");
            post03.setAccount(account03);

            Post post04 = new Post();
            post04.setTitle("HP Inc.");
            post04.setBody("Supplier of computers, laptops, and printers");
            post04.setAccount(account02);

            Post post05 = new Post();
            post05.setTitle("Intel");
            post05.setBody("Supplier of microprocessors and semiconductor products");
            post05.setAccount(account03);

            Post post06 = new Post();
            post06.setTitle("AMD");
            post06.setBody("Supplier of graphics cards, processors, and semiconductor products");
            post06.setAccount(account01);

            Post post07 = new Post();
            post07.setTitle("NVIDIA");
            post07.setBody("Supplier of GPUs, AI solutions, and cloud computing hardware");
            post07.setAccount(account02);

            Post post08 = new Post();
            post08.setTitle("Cisco Systems");
            post08.setBody("Supplier of networking hardware and telecommunications equipment");
            post08.setAccount(account01);

            Post post09 = new Post();
            post09.setTitle("Qualcomm");
            post09.setBody("Supplier of mobile processors and wireless technology");
            post09.setAccount(account03);

            Post post10 = new Post();
            post10.setTitle("Samsung Electronics");
            post10.setBody("Supplier of semiconductors, display panels, and electronic components");
            post10.setAccount(account02);

            Post post11 = new Post();
            post11.setTitle("Sony");
            post11.setBody("Supplier of consumer electronics and imaging sensors");
            post11.setAccount(account03);

            Post post12 = new Post();
            post12.setTitle("LG Electronics");
            post12.setBody("Supplier of home appliances, display panels, and electronic components");
            post12.setAccount(account01);

            Post post13 = new Post();
            post13.setTitle("Texas Instruments");
            post13.setBody("Supplier of embedded processors and analog semiconductor products");
            post13.setAccount(account03);

            Post post14 = new Post();
            post14.setTitle("Micron Technology");
            post14.setBody("Supplier of memory and storage solutions");
            post14.setAccount(account01);

            Post post15 = new Post();
            post15.setTitle("Western Digital");
            post15.setBody("Supplier of hard drives, SSDs, and storage solutions");
            post15.setAccount(account02);

            Post post16 = new Post();
            post16.setTitle("Seagate Technology");
            post16.setBody("Supplier of hard drives and data storage solutions");
            post16.setAccount(account03);

            Post post17 = new Post();
            post17.setTitle("IBM");
            post17.setBody("Supplier of enterprise solutions, cloud computing, and AI technology");
            post17.setAccount(account01);

            Post post18 = new Post();
            post18.setTitle("Foxconn");
            post18.setBody("Supplier of electronics manufacturing and assembly services");
            post18.setAccount(account02);

            Post post19 = new Post();
            post19.setTitle("Panasonic");
            post19.setBody("Supplier of consumer electronics and industrial solutions");
            post19.setAccount(account03);

            Post post20 = new Post();
            post20.setTitle("Toshiba");
            post20.setBody("Supplier of energy systems, storage devices, and industrial solutions");
            post20.setAccount(account01);

            Post post21 = new Post();
            post21.setTitle("Sharp Corporation");
            post21.setBody("Supplier of display technology and electronic components");
            post21.setAccount(account02);

            Post post22 = new Post();
            post22.setTitle("Lenovo");
            post22.setBody("Supplier of computers, tablets, and IT solutions");
            post22.setAccount(account03);

            Post post23 = new Post();
            post23.setTitle("BOE Technology");
            post23.setBody("Supplier of LCD and OLED display panels");
            post23.setAccount(account01);

            Post post24 = new Post();
            post24.setTitle("ASUS");
            post24.setBody("Supplier of laptops, motherboards, and gaming peripherals");
            post24.setAccount(account02);

            Post post25 = new Post();
            post25.setTitle("MSI");
            post25.setBody("Supplier of gaming laptops, motherboards, and graphics cards");
            post25.setAccount(account03);

            Post post26 = new Post();
            post26.setTitle("Dell EMC");
            post26.setBody("Supplier of enterprise storage and cloud solutions");
            post26.setAccount(account01);

            Post post27 = new Post();
            post27.setTitle("Epson");
            post27.setBody("Supplier of printers, scanners, and imaging solutions");
            post27.setAccount(account02);

            Post post28 = new Post();
            post28.setTitle("Canon");
            post28.setBody("Supplier of imaging, optical products, and office solutions");
            post28.setAccount(account03);

            // Save all posts
            postService.save(post01);
            postService.save(post02);
            postService.save(post03);
            postService.save(post04);
            postService.save(post05);
            postService.save(post06);
            postService.save(post07);
            postService.save(post08);
            postService.save(post09);
            postService.save(post10);
            postService.save(post11);
            postService.save(post12);
            postService.save(post13);
            postService.save(post14);
            postService.save(post15);
            postService.save(post16);
            postService.save(post17);
            postService.save(post18);
            postService.save(post19);
            postService.save(post20);
            postService.save(post21);
            postService.save(post22);
            postService.save(post23);
            postService.save(post24);
            postService.save(post25);
            postService.save(post26);
            postService.save(post27);
            postService.save(post28);

        }

    }

}
