# README.md for Your Application

## Overview
This application is designed to create and manage orders by interacting with a simple REST API. Users can add products to their cart, view the cart contents, remove items from the cart, and place orders. The application is integrated with Stripe for payment processing and uses MongoDB to store order data.

## Features
- **Add Products to Cart:** Send a POST request with product details to add items to the cart.
- **View Cart:** Retrieve the current contents of a cart with a GET request.
- **Remove Products from Cart:** Delete items from the cart using a specific endpoint.
- **Place Orders:** Create an order from the cart items, which clears the cart and processes payment through Stripe.

## API Endpoints

### Add to Cart
- **URL**: `POST http://localhost:8080/carts/{userId}/add`
- **Payload Example**:
  ```json
  {
    "product": {
      "id": "prod1",
      "name": "Product 1",
      "description": "Description 1",
      "price": 10
    },
    "quantity": 5
  }
  ```

### View Cart
- **URL**: `GET http://localhost:8080/carts/{userId}`

### Remove from Cart
- **URL**: `DELETE http://localhost:8080/carts/{userId}/remove/{prodId}`

### Place Order
- **URL**: `POST http://localhost:8080/orders/place/{userId}`

## Integration with Stripe
- The application is integrated with Stripe for payment processing.
- The `paymentIntentId` is recorded in MongoDB when an order is placed.

## Getting Started

### Prerequisites
- Stripe account and API key.

### Setting Up
1. **Stripe API Key**: Set your Stripe API key in `application.properties`:
   ```
   STRIPE_API_KEY=your key
   ```
2. **Running the Application**: Use the following command to start the application:
   ```bash
   gradle bootRun
   ```
