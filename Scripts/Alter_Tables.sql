
-----------------------------------------------------------
 -- Adding Field To table : price_info_dtail
 -- fieild name : price_info_id
DO
$$
BEGIN
if not exists(
SELECT column_name
FROM information_schema.columns
WHERE table_schema='public'
  and table_name='price_info_dtail'
  and column_name='price_info_id'
  )
  then

 ALTER TABLE price_info_dtail add column price_info_id int NOT NULL ;

 ALTER TABLE price_info_dtail
  ADD CONSTRAINT fk_priceinfodtail_price_info_id FOREIGN KEY (price_info_id)
      REFERENCES price_info (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

  RAISE NOTICE 'price_info_id is added to price_info_dtail';


END IF ;
END
$$
-------------------------------------------------------------------------------------------

-- new table service_item_price_info
DO
$$
BEGIN
if not exists(
SELECT column_name
FROM information_schema.columns
WHERE table_schema='public'
  and table_name='service_item_price_info'
  )
  then

CREATE TABLE service_item_price_info
(
  service_items_id 		bigint NOT NULL,
  price_info_id 		bigint NOT NULL,

  CONSTRAINT service_item_price_info_pkey PRIMARY KEY (service_items_id, price_info_id),
  CONSTRAINT fk_service_item_price_info_serviceitems_id FOREIGN KEY (service_items_id)
      REFERENCES service_item (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_service_item_price_info_priceinfo_id FOREIGN KEY (price_info_id)
      REFERENCES price_info (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE service_item_price_info
  OWNER TO postgres;


    ALTER TABLE service_item DROP COLUMN priceinfo_id;

  RAISE NOTICE 'table service-item-price-info is created';


END IF ;
END
$$
------------------------------------------------------------------------
 -- Adding Field To table : servant
 -- fieild name : company_id
DO
$$
BEGIN
if not exists(
SELECT column_name
FROM information_schema.columns
WHERE table_schema='public'
  and table_name='servant'
  and column_name='company_id'
  )
  then

 ALTER TABLE servant add column company_id int NOT NULL ;

 ALTER TABLE servant
  ADD CONSTRAINT fk_servant_company_id FOREIGN KEY (company_id)
      REFERENCES company (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

  RAISE NOTICE 'company_id is added to servant';


END IF ;
END
$$
