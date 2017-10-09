CREATE TABLE [dbo].Restaurant
(
	[RestaurantId] INT NOT NULL PRIMARY KEY, 
    [Name] VARCHAR(15) NOT NULL, 
    [Description] VARCHAR(30) NULL, 
    [Address] VARCHAR(30) NOT NULL, 
    [ImageURL] VARCHAR(30) NOT NULL, 
    [Stars] INT NULL DEFAULT 0
)
