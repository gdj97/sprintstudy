package dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item {
	private int id;
	private String name;
	private int price;
	private String description;
	private String pictureUrl;
	private MultipartFile picture;
}
