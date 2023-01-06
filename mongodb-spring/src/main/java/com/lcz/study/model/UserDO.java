package com.lcz.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("User")
public class UserDO {

    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String createDate;

}
