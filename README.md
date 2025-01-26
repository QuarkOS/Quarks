# QuarkOS Shop Bot

QuarkOS Shop Bot is a Discord bot that allows users to interact with a virtual shop. Users can view available items, add items to their cart, view their cart, and proceed to checkout.

## Features

- View available items in the shop
- Add items to the cart
- View items in the cart
- Confirm and cancel checkout
- Autocomplete for product names

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- A Discord bot token

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/QuarkOS/ShopBot.git
    cd ShopBot
    ```

2. Set up your environment variables:
    Create a `.env` file in the root directory and add your Discord bot token:
    ```env
    DISCORD_TOKEN=your_discord_token_here
    ```

3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

4. Run the bot:
    ```sh
    java -jar target/ShopBot-1.0-SNAPSHOT.jar
    ```

## Usage

### Commands

- `/shop viewitems` - View available items in the shop
- `/shop additem <item_name>` - Add an item to the cart
- `/shop viewcart` - View items in the cart
- `/shop checkout` - Confirm the checkout
- `/shop cancel` - Cancel the checkout

### Autocomplete

The bot provides autocomplete suggestions for product names when using the `/shop additem` command.

## Configuration

The bot configuration is managed through the `Config` class. The `Config` class uses the `Dotenv` library to load environment variables.
