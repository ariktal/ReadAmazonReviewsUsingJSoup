package org.jsoup.examples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;


/**
 * A simple example, used on the jsoup website.
 */
public class ReadAmazonReviewsUsingJSoup {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.amazon.com/dp/B004R69AG8?th=1/").get();

        log(doc.title());

        log("********** Print Main Page Reviews ************\n" );
        printReviews(doc);


        log("Print All Reviews - get the link to all reviews and then iterate each time to the next page to get the next reviews " +
                  "calling printAllReviews(a recursion function), it doesn't work well from performance perspective seems that it is better to find a way " +
                  "to get all reviews in one time removing the overhand of connecting to a new url each time in order to get the next page\n" );

        Element review = doc.getElementById("dp-summary-see-all-reviews");
        String hrefLink = review.attr("abs:href");
        log(hrefLink);

        log("********** Print All pages ************\n" );

        printAllReviews(hrefLink);

    }


    private static void printAllReviews ( String link )  throws IOException {

        if(link !=null && !link.equals("")) {
            Document reviewsPage= null;
            try {
                reviewsPage= Jsoup.connect(link).get();
            }catch (Exception e){
                log(e.getMessage());
                throw e ;
            }

            printReviews(reviewsPage);
                //get the next page with reviews element
                Element nextPageElement = reviewsPage.getElementsByClass("a-last").first();
                if (nextPageElement !=null ) {
                    //get only relevant  element
                    Elements nextPageLink = nextPageElement.select("a[href]");
                    if (nextPageLink!=null && !nextPageLink.isEmpty()) {
                        //get next page link as string
                        String nextPage = nextPageLink.attr("abs:href");
                        log(nextPage);
                        // recursion recursion
                        printAllReviews(nextPage);
                    }
                }
        }

    }


    private static  void printReviews ( Document doc) {

        Elements reviewElements = doc.select(".review");
        if (reviewElements == null || reviewElements.isEmpty()) {
            log("Didn't find reviews");
        }

        for (Element reviewElement : reviewElements) {
            Element titleElement = reviewElement.select(".review-title").first();
            if (titleElement == null) {
                continue;
            }
            String title = titleElement.text();
            Element textElement = reviewElement.select(".review-text").first();
            if (textElement == null) {
                continue;
            }
            String text = textElement.text();
            log("Print Review\n title: %s\n text:%s\n", title, text);

        }
    }

    private static void log(String msg, String... vals) {
        System.out.println(String.format(msg, vals));
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }




}
