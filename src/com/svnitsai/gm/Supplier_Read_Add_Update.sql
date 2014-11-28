SELECT S.[CustCode]
      ,[CustName]
      ,[CustAddress1]
      ,[CustAddress2]
      ,[CustCity]
      ,[CustContactNumber]
      ,[CustExtension]
      ,[CustBank]
      ,[CustBankBranch]
      ,[CustBankAccountType]
      ,[CustBankAccountNumber]
  FROM [Customer] S join [CustomerBanks] SB on s.custcode = SB.CustCode and s.custtype = 'Supplier'
Order by S.Custname