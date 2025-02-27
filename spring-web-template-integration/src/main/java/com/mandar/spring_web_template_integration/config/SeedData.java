package com.mandar.spring_web_template_integration.config;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Authority;
import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.services.AuthorityService;
import com.mandar.spring_web_template_integration.services.PostService;
import com.mandar.spring_web_template_integration.util.constants.Privilege;
import com.mandar.spring_web_template_integration.util.constants.Roles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWORD = "pass787";
    private static final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initializing seed data...");

        // Initialize authorities
        for (Privilege privilege : Privilege.values()) {
            Authority authority = new Authority();
            authority.setId(privilege.getId());
            authority.setName(privilege.getPrivilegeString());
            authorityService.save(authority);
        }

        Set<Authority> allAuthorities = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(allAuthorities::add);
        authorityService.findById(Privilege.RESET_ANY_USER_PASSWORD.getId()).ifPresent(allAuthorities::add);

        Set<Authority> adminPanelAuthority = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(adminPanelAuthority::add);

        // Initialize accounts
        Account account01 = createAccount("admin@iprocure.com", "admin", "admin", Roles.ADMIN.getRole(), adminPanelAuthority);
        Account account02 = createAccount("editor@iprocure.com", "editor", "editor", Roles.EDITOR.getRole(), new HashSet<>());
        Account account03 = createAccount("user@iprocure.com", "user", "user", null, new HashSet<>());
        Account account04 = createAccount("super_editor@iprocure.com", "super_editor", "super_editor", Roles.EDITOR.getRole(), allAuthorities);

        account01.setPassword(DEFAULT_PASSWORD);
        account02.setPassword(DEFAULT_PASSWORD);
        account03.setPassword(DEFAULT_PASSWORD);
        account04.setPassword(DEFAULT_PASSWORD);


        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);

        // Initialize posts if none exist
        List<Post> posts = postService.getAll();
        if (posts.isEmpty()) {
            createAndSavePosts(account01, account02, account03);
        }

        log.info("Seed data initialization completed.");
    }

    private Account createAccount(String email, String firstname, String lastname, String role, Set<Authority> authorities) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setRole(role);
        account.setAuthorities(authorities);
        return account;
    }

    private void createAndSavePosts(Account adminAccount, Account editorAccount, Account userAccount) {
        Post post01 = createPost("Rockwell Automation", "Supplier of industrial automation and information technology", adminAccount, "United States");
        Post post02 = createPost("Oracle", "Supplier of database software and cloud engineered systems", editorAccount, "United States");
        Post post03 = createPost("Qualcomm", "Supplier of mobile processors and wireless technology", userAccount, "United States");
        Post post04 = createPost("HP Inc.", "Supplier of computers, laptops, and printers", editorAccount, "United States");
        Post post05 = createPost("Intel", "Supplier of microprocessors and semiconductor products", userAccount, "United States");
        Post post06 = createPost("AMD", "Supplier of graphics cards, processors, and semiconductor products", adminAccount, "United States");
        Post post07 = createPost("NVIDIA", "Supplier of GPUs, AI solutions, and cloud computing hardware", editorAccount, "United States");
        Post post08 = createPost("Cisco Systems", "Supplier of networking hardware and telecommunications equipment", adminAccount, "United States");
        Post post09 = createPost("Samsung Electronics", "Supplier of semiconductors, display panels, and electronic components", editorAccount, "South Korea");
        Post post10 = createPost("Sony", "Supplier of consumer electronics and imaging sensors", userAccount, "Japan");
        Post post11 = createPost("LG Electronics", "Supplier of home appliances, display panels, and electronic components", adminAccount, "South Korea");
        Post post12 = createPost("Texas Instruments", "Supplier of embedded processors and analog semiconductor products", userAccount, "United States");
        Post post13 = createPost("Micron Technology", "Supplier of memory and storage solutions", adminAccount, "United States");
        Post post14 = createPost("Western Digital", "Supplier of hard drives, SSDs, and storage solutions", editorAccount, "United States");
        Post post15 = createPost("Seagate Technology", "Supplier of hard drives and data storage solutions", userAccount, "United States");
        Post post16 = createPost("IBM", "Supplier of enterprise solutions, cloud computing, and AI technology", adminAccount, "United States");
        Post post17 = createPost("Foxconn", "Supplier of electronics manufacturing and assembly services", editorAccount, "Taiwan");
        Post post18 = createPost("Panasonic", "Supplier of consumer electronics and industrial solutions", userAccount, "Japan");
        Post post19 = createPost("Toshiba", "Supplier of energy systems, storage devices, and industrial solutions", adminAccount, "Japan");
        Post post20 = createPost("Sharp Corporation", "Supplier of display technology and electronic components", editorAccount, "Japan");
        Post post21 = createPost("Lenovo", "Supplier of computers, tablets, and IT solutions", userAccount, "China");
        Post post22 = createPost("BOE Technology", "Supplier of LCD and OLED display panels", adminAccount, "China");
        Post post23 = createPost("ASUS", "Supplier of laptops, motherboards, and gaming peripherals", editorAccount, "Taiwan");
        Post post24 = createPost("MSI", "Supplier of gaming laptops, motherboards, and graphics cards", userAccount, "Taiwan");
        Post post25 = createPost("Dell EMC", "Supplier of enterprise storage and cloud solutions", adminAccount, "United States");
        Post post26 = createPost("Epson", "Supplier of printers, scanners, and imaging solutions", editorAccount, "Japan");
        Post post27 = createPost("Canon", "Supplier of imaging, optical products, and office solutions", userAccount, "Japan");

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
    }

    private Post createPost(String title, String body, Account account, String country) {
        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        post.setAccount(account);
        post.setCountry(country);
        post.setSapId(generateRandomSapId());
        return post;
    }

    private String generateRandomSapId() {
        // Generate a random 4-digit number
        int sapId = random.nextInt(9000) + 1000; // Range: 1000 to 9999
        return String.valueOf(sapId);
    }
}