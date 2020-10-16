package oss.pilot;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class Player implements Serializable {

    private String name;
    private Long score;

}
