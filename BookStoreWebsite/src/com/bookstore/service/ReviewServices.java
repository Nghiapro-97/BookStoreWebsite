package com.bookstore.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.ReviewDAO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Customer;
import com.bookstore.entity.Review;

public class ReviewServices {
	private ReviewDAO reviewDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public ReviewServices(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
		this.reviewDAO=new ReviewDAO();
	}
	
	public void listAllReview() throws ServletException, IOException {
		listAllReview(null);
	}

	public void listAllReview(String message) throws ServletException, IOException {
		
		List<Review> listReviews=reviewDAO.listAll();
		request.setAttribute("listReviews", listReviews);
		
		if (message != null) {
			request.setAttribute("message", message);
		}
		
		CommonUtility.forwardToPage("review_list.jsp", request, response);
	}

	public void editReview() throws ServletException, IOException {
		
		Integer reviewId=Integer.parseInt(request.getParameter("id"));
		Review review=reviewDAO.get(reviewId);
		
		if (review != null) {		
			request.setAttribute("review", review);		
			CommonUtility.forwardToPage("review_form.jsp", request, response);
		} else {
			String message = "Could not find review with ID " + reviewId;
			CommonUtility.showMessageBackend(message, request, response);
		}
	}

	public void updateReview() throws ServletException, IOException {
		
		Integer reviewId=Integer.parseInt(request.getParameter("reviewId"));
		String headline=request.getParameter("headline");
		String comment=request.getParameter("comment");
		
		Review review=reviewDAO.get(reviewId);
		review.setHeadline(headline);
		review.setComment(comment);
		
		reviewDAO.update(review);
		String message="the update review successfull!";
		listAllReview(message);
	}

	public void deleteReview() throws ServletException, IOException {
		Integer reviewId=Integer.parseInt(request.getParameter("id"));
		Review review = reviewDAO.get(reviewId);
		
		if (review != null) {
			reviewDAO.delete(reviewId);
			String message = "The review has been deleted successfully.";
			listAllReview(message);
		} else {
			String message = "Could you find review with ID " + reviewId
					+ ", or it might have been deleted by another admin";
			CommonUtility.showMessageBackend(message, request, response);
		}	
	}

	public void showReviewForm() throws ServletException, IOException {
		
		Integer bookId=Integer.parseInt(request.getParameter("book_id"));
		BookDAO bookDAO = new BookDAO();
		Book book=bookDAO.get(bookId);
		
		HttpSession session=request.getSession();
		session.setAttribute("book", book);
		
		Customer customer=(Customer) session.getAttribute("loggedCustomer");
		
		Review existReview = reviewDAO.findByCustomerAndBook(customer.getCustomerId(), bookId);
		String targetPage="frontend/review_form.jsp";
		
		if (existReview != null) {
			request.setAttribute("review", existReview);
			targetPage="frontend/review_info.jsp";
		}
		
		CommonUtility.forwardToPage(targetPage, request, response);
	}

	public void submitReview() throws ServletException, IOException {
		Integer bookId=Integer.parseInt(request.getParameter("bookId"));
		Integer rating=Integer.parseInt(request.getParameter("rating"));
		String headline=request.getParameter("headline");
		String comment=request.getParameter("comment");
		
		Review newreview=new Review();
		newreview.setHeadline(headline);
		newreview.setComment(comment);
		newreview.setRating(rating);
		
		Book book = new Book();
		book.setBookId(bookId);
		newreview.setBook(book);
		
		Customer customer=(Customer) request.getSession().getAttribute("loggedCustomer");
		newreview.setCustomer(customer);
		
		reviewDAO.create(newreview);
		
		CommonUtility.forwardToPage("frontend/review_done.jsp", request, response);
	}

}
