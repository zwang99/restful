# restful
## User
- If the user has entered the correct credentials, they may proceed to the corresponding page based on their authorities.
- The user is able to view all of the products. An out of stock product should NOT be shown to the user.
- After purchasing the product, the user should be able to view order details including, order placement time and order status which is Processing, Completed or Canceled.
- The user should be able to purchase listing items with a specified quantity by creating a “Processing” order. After a user places an order, the item’s stock should be deducted accordingly.
- The user can add/remove products to/from their watchlist.
- The user should be able to view all their orders.
- The user can click and look into any one specific order created by them, completed with the items included in that order.
- The user should be able to view their top 3 most frequently purchased items. (excluding canceled order, use item ID as tie breaker).

## Screenshoots
1. Home page
![Image](https://github.com/zwang99/search/blob/main/images/home.png)


2. Runtime using ElasticSearch
![Image](https://github.com/zwang99/search/blob/main/images/elastic.png)

3. Runtime using Hadoop generated inverted index
![Image](https://github.com/zwang99/search/blob/main/images/hadoop.png)

## Acknowledgements
Course project collaborate with teammate Chenhao Pan
