package oss.member.model.google;


import lombok.Data;

@Data
public class Recaptcha {
	String action;
	String challenge_ts;
	String hostname;
	double score;
	String success;
}
