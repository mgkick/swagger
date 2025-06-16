package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo ;   // 입력말기..

    @Column(unique = true)
    private String  id;      //--------------------------------
    private String  pwd;      //--------------------------------

    @Column(length = 20)
    private String  name;      //--------------------------------
    private String  address;    //-----------DTO 만들기 4개만 따로

    private String role;

    @CreationTimestamp
    private LocalDateTime regDate;

}
