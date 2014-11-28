SELECT DC.[PayCReferenceNumber]
      ,DC.[PayCDueDate]
      ,DC.[CustCode]
      ,DC.[InvoiceAmount]
      ,DC.[PayCStatus]
      ,DC.[InvoiceReferenceNumber]
      ,DC.[DeferredDate]
      ,DCD.[PayCReferenceSubNumber]
      ,DCD.[PayCDate]
      ,DCD.[CustBankId]
      ,DCD.[SupplierCode]
      ,DCD.[SupplierBankId]
      ,DCD.[PaidAmount]
      ,DCD.[AccountLocationCode]
      ,DCD.[LedgerPageNumber]
	  ,c.[CustName]
	  ,C.[CustContactNumber]
,CB.[CustBank]
,SupplierBank = CB1.[CustBank]
,SupplierName = S.Custname
  FROM [DailyPayC] DC join [DailyPayCDetails] DCD 
ON DC.[PayCReferenceNumber] = DCD.[PayCReferenceNumber]
join Customer c on c.custcode = dc.custcode and c.custtype = 'Merchant'
left join CustomerBanks CB on CB.CustBankId = DCD.CustBankId and c.custcode = cb.custcode
left join Customer s on s.custcode = dcd.suppliercode and s.custtype = 'Supplier' 
left join CustomerBanks CB1 on CB1.CustBankId = DCD.SupplierBankId and s.custcode = cb1.custcode
left join Customer CO on CO.custcode = dcd.accountlocationcode and CO.custtype = 'Company' 
where (DC.DeferredDate is not null and DC.DeferredDate <= getdate()) or 
(DC.DeferredDate is null and DC.PayCDueDate <= getdate())