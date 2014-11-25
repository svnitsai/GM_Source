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
      ,DCD.[SupplierId]
      ,DCD.[SupplierBankId]
      ,DCD.[PaidAmount]
      ,DCD.[AccountLocationId]
      ,DCD.[LedgerPageNumber]
	  ,c.[CustName]
	  ,C.[CustContactNumber]
,CB.[CustBank]
,SupplierBank = CB1.[CustBank]
  FROM [DailyPayC] DC join [DailyPayCDetails] DCD 
ON DC.[PayCReferenceNumber] = DCD.[PayCReferenceNumber]
join Customer c on c.custcode = dc.custcode and c.custtype = 'Merchant'
left join CustomerBanks CB on CB.CustBankId = DCD.CustBankId and c.custid = cb.custid
left join Customer s on s.custcode = dcd.supplierid and s.custtype = 'Supplier' 
left join CustomerBanks CB1 on CB1.CustBankId = DCD.SupplierBankId and s.custid = cb1.custid
left join Customer CO on CO.custcode = dcd.accountlocationid and CO.custtype = 'Company' 
where (DC.DeferredDate is not null and DC.DeferredDate <= getdate()) or 
(DC.DeferredDate is null and DC.PayCDueDate <= getdate())