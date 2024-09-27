package com.virtualfilesystem.VirtualFileSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class VirtualFileSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualFileSystemApplication.class, args);
	}

}
