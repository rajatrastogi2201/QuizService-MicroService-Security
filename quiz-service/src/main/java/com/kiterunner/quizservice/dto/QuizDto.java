package com.kiterunner.quizservice.dto;

import lombok.Data;

@Data
public class QuizDto {
	
	String categoryName;
	Integer numQuestions;
	String title;
}
