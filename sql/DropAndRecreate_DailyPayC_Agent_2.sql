UPDATE [DailyPayCDetails]
   SET [PayCReferenceNumber] = DailyPayC1.PayCReferenceNumber,
   [UpdatedDate] = GETDATE(),
   [UpdatedBy] = 'SQL Update'
FROM [DailyPayC1]
WHERE [DailyPayCDetails].PayCReferenceNumber = [DailyPayC1].OldPayCReferenceNumber

GO


Select dd.PayCReferenceSubNumber, dd.[PayCReferenceNumber],D.PayCReferenceNumber as newpaycrn,
d.oldpaycreferencenumber as oldpaycrn, 
dd.[UpdatedDate],dd.[UpdatedBy]
FROM [DailyPayCDetails] DD
join dailypayc1 d on d.oldpaycreferencenumber = dd.PayCReferenceNumber 

GO
Select dd.PayCReferenceSubNumber, dd.[PayCReferenceNumber],D.PayCReferenceNumber as newpaycrn,
d.oldpaycreferencenumber as oldpaycrn, 
dd.[UpdatedDate],dd.[UpdatedBy]
FROM [DailyPayCDetails] DD
join dailypayc1 d on d.paycreferencenumber = dd.PayCReferenceNumber 

GO