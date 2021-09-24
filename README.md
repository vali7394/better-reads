# better-reads
An App to find and read books you'll love, and keep track of the books you want to read. This is similar to https://www.goodreads.com/ and designing and developing this for learning purpose.

The app design is more backend centric hence the UI (UX) is very basic. 

**System Design :**

[![Screen-Shot-2021-09-22-at-11-34-36-AM.png](https://i.postimg.cc/rssm8zXj/Screen-Shot-2021-09-22-at-11-34-36-AM.png)](https://postimg.cc/gL9dsz0L)

**ERD**

[![Screen-Shot-2021-09-23-at-9-29-37-AM.png](https://i.postimg.cc/fbHstY9Y/Screen-Shot-2021-09-23-at-9-29-37-AM.png)](https://postimg.cc/JyBvSDKh)

**Cassandra DataModeling :**

Best Practice to design the datamodeling in cassandra by doing the access patterns[i.e. queries used in the code]. Below are the queries we use in the application to access the data to present to the user.

```json
1) BookBy Id
Table BooksById {
  bookId  [pk]
  name 
  desc 
  cover 
  authorName
  authorId
  
 }
 2) Book By Author ID
Table AuthorById {
  authorId [pk]
  publishedDate [c]
  name
  bookId
  bookName
  cover
}
3) UserBook by user id & Book Id
Table UserBook {
  userId  [pk] 
  bookId  [pk]
  status varchar
  start
  end
  rating 
  status
  }
4) userBooks by userId
Table User{
  userId [pk] 
  status[c]
  TimeUUid [c]
  bookId 
  rating
}
```
