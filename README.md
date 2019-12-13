# GenerateScriptsFromExcel

Url :https://generatescripts.run.aws-usw02-pr.ice.predix.io/generatescripts
From Users perspective Indexes starts with 1.
Every Request Param mandatory to be selected . If you dont want them you can make their values as blank.

Request Params :
1.file -- upload a file
2.tableName ---Name of the table
3.startingRowNum -- this is optional  .If you left this as blank it will take default Row as 1
4.limitRows  -- this is optional . If you left this as blank it will take default as total Number of Rows in Excel
5.integerColumns -- Give the Index of Integer Columns as Comma Seperated
6.charactersToReplace  -- Give the special Characters that you want to replace. This is not mandatory you can left the value as blank if you dont want to replace 
7.charactersToReplaceWith -- Give the escaping character to replace special character with .This is not mandatory you can left the value as blank if you dont have special characters

By Default if you dont give charactersToReplace and charactersToReplaceWith it will check for SingleQuote(') and will replace it with ('') if present


You Can Clone the Project and you can import Postman Collection and you can start using the service
