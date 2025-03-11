# MongoDB Sample Data Import Instructions

This file contains instructions for importing the sample user data into MongoDB for testing purposes.

## Sample Data Format

The sample users have been simplified to just include name and email fields. The application will automatically generate MongoDB ObjectId values for the `_id` field when inserting these records.

## Prerequisites

- MongoDB installed and running locally on port 27017 (or configured according to your application.yml)
- MongoDB command line tools (mongoimport)

## Import Methods

### Using mongoimport (Recommended)

```bash
# From the root of the project
mongoimport --db userdb --collection users --file src/main/resources/sample-users.json --jsonArray
```

### Using MongoDB Compass

1. Open MongoDB Compass
2. Connect to your MongoDB instance
3. Create/select the "userdb" database
4. Create/select the "users" collection
5. Click on "Add Data" > "Import File"
6. Select the sample-users.json file
7. Choose "JSON" as the file type
8. Click "Import"

### Using Mongo Shell

```bash
# Start the MongoDB shell
mongo

# Switch to the userdb database
use userdb

# Insert the users (copy and paste from the sample-users.json file)
db.users.insertMany([
  // Content of the sample-users.json file
]);
```

## Verification

To verify that the data was imported correctly:

```bash
# Using mongo shell
mongo

# Switch to the userdb database
use userdb

# Query all users
db.users.find().pretty()
```

You should see all 10 sample users in the collection, and MongoDB will automatically generate ObjectId values for these records.