



INSERT INTO [PayC].[dbo].[Customer]
           ([CustCode]          ,[CustType]
           ,[CustName]           ,[CustAddress1]
           ,[CustAddress2]           ,[CustAddress3]
           ,[CustAddress4]           ,[CustCity]
           ,[CustState]           ,[CustCountry]
           ,[CustContactNumber]           ,[CustExtension]
           ,[CreatedDate]           ,[CreatedBy]
           ,[UpdatedDate]           ,[UpdatedBy])
Select 100000000,
N'Merchant',
N'Merchant 100000000',
N'1 Main Road',
N'Add 2',
N'Add 3',
N'Add 4',
N'Coimbatore',
N'TN',
N'India',
1234567890,
0000000,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 120000000,
N'Merchant',
N'Merchant 120000000',
N'2 Main Road',
N'Add 2',
N'Add 3',
N'Add 4',
N'Salem',
N'TN',
N'India',
2345678901,
0000000,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 130000000,
N'Merchant',
N'Merchant 130000000',
N'3 Main Road',
N'Add 2',
N'Add 3',
N'Add 4',
N'Erods',
N'TN',
N'India',
3456789012,
0000000,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'Merchant',
N'Merchant 140000000',
N'4 Main Road',
N'Add 2',
N'Add 3',
N'Add 4',
N'Trichy',
N'TN',
N'India',
4567890123,
0000000,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 150000000,
N'Merchant',
N'Merchant 150000000',
N'5 Main Road',
N'Add 2',
N'Add 3',
N'Add 4',
N'Madurai',
N'TN',
N'India',
5678901234,
0000000,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'







INSERT INTO [PayC].[dbo].[DailyPayC]
           ([PayCDueDate]
           ,[CustCode]
           ,[InvoiceAmount]
           ,[PayCStatus]
           ,[InvoiceReferenceNumber]
           ,[DeferredDate]
           ,[CreatedDate]
           ,[CreatedBy]
           ,[UpdatedDate]
           ,[UpdatedBy])
Select 
convert(varchar, getdate(), 113),
100000000,
10000,
null,
1,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select convert(varchar, getdate(), 113),
120000000,
20000,
null,
2,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select convert(varchar, getdate(), 113),
130000000,
30000,
null,
3,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select convert(varchar, getdate(), 113),
140000000,
40000,
null,
4,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select convert(varchar, getdate(), 113),
150000000,
50000,
null,
5,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'




INSERT INTO [PayC].[dbo].[DailyPayCDetails]
           ([PayCRefereneNumber]            ,[PayCReferenceSubNumber]
           ,[PayCDate]           ,[CustBankId]
           ,[SupplierCode]           ,[SupplierBankId]
           ,[PaidAmount]           ,[AccountLocationCode]
           ,[LedgerPageNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 1,
1,
convert(varchar, getdate(), 113),
null,
null,
null,
null,
null,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 2,
1,
convert(varchar, getdate(), 113),
null,
null,
null,
null,
null,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 3,
1,
convert(varchar, getdate(), 113),
null,
null,
null,
null,
null,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 4,
1,
convert(varchar, getdate(), 113),
null,
null,
null,
null,
null,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 5,
1,
convert(varchar, getdate(), 113),
null,
null,
null,
null,
null,
null,
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'


INSERT INTO [PayC].[dbo].[CustomerBanks]
           ([CustCode]         ,[CustBank]
           ,[CustBankBranch]           ,[CustBankAccountType]
           ,[CustBankAccountNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 100000000,
N'State Bank of India',
N'Coimbatore Main',
N'Current',,
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 100000000,
N'HDFC Bank',
N'Coimbatore Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 100000000,
N'ICICI Bank',
N'Coimbatore Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 100000000,
N'Indian Bank',
N'Coimbatore Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 100000000,
N'Indian Overseas Bank',
N'Coimbatore Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'


INSERT INTO [PayC].[dbo].[CustomerBanks]
           ([CustCode]         ,[CustBank]
           ,[CustBankBranch]           ,[CustBankAccountType]
           ,[CustBankAccountNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 120000000,
N'State Bank of India',
N'Salem Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 120000000,
N'HDFC Bank',
N'Salem Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 120000000,
N'ICICI Bank',
N'Salem Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 120000000,
N'Indian Bank',
N'Salem Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 120000000,
N'Indian Overseas Bank',
N'Salem Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'




INSERT INTO [PayC].[dbo].[CustomerBanks]
           ([CustCode]         ,[CustBank]
           ,[CustBankBranch]           ,[CustBankAccountType]
           ,[CustBankAccountNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 130000000,
N'State Bank of India',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 130000000,
N'HDFC Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 130000000,
N'ICICI Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 130000000,
N'Indian Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 130000000,
N'Indian Overseas Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'




INSERT INTO [PayC].[dbo].[CustomerBanks]
           ([CustCode]         ,[CustBank]
           ,[CustBankBranch]           ,[CustBankAccountType]
           ,[CustBankAccountNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 140000000,
N'State Bank of India',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'HDFC Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'ICICI Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'Indian Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'Indian Overseas Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'





INSERT INTO [PayC].[dbo].[CustomerBanks]
           ([CustCode]         ,[CustBank]
           ,[CustBankBranch]           ,[CustBankAccountType]
           ,[CustBankAccountNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 140000000,
N'State Bank of India',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'HDFC Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'ICICI Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'Indian Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 140000000,
N'Indian Overseas Bank',
N'Trichy Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'




INSERT INTO [PayC].[dbo].[CustomerBanks]
           ([CustId]         ,[CustBank]
           ,[CustBankBranch]           ,[CustBankAccountType]
           ,[CustBankAccountNumber]           ,[CreatedDate]
           ,[CreatedBy]           ,[UpdatedDate]
           ,[UpdatedBy])
Select 200000000,
N'State Bank of India',
N'Tiruppur Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 200000000,
N'HDFC Bank',
N'Tiruppur Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 200000000,
N'ICICI Bank',
N'Tiruppur Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 200000000,
N'Indian Bank',
N'Tiruppur Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'
union all
Select 200000000,
N'Indian Overseas Bank',
N'Tiruppur Main',
N'Current',
N'1111222233334444',
convert(varchar, getdate(), 113),
N'ManualLoad',
convert(varchar, getdate(), 113),
N'ManualLoad'


