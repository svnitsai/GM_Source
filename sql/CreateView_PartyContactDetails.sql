USE PayC
GO

Create View PartyContactDetails AS
SELECT     PTYCODE, PTYNAME, PHONE1, PHONE2, RESIDENCY, CELL, EMAIL, Messageno, Ownername
FROM         dbo.PARTY
WITH CHECK OPTION


