update page_element
set key = 'presentation.date'
where id in (
  select pe.id
  from page_element pe
         join page p on pe.page_id = p.id
  where p.model_name = 'report'
    and pe.key = 'presentation.author'
);
