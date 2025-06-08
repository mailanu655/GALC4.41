--select statement to see if this property is set for the client
SELECT * FROM galadm.GAL489TBX where PROPERTY_KEY = 'ALLOW_MIXED_HEAD_BLOCK';

--If set need to update that with new property
UPDATE galadm.gal489tbx set PROPERTY_KEY = 'ALLOW_MIXED_PRODUCTS' where PROPERTY_KEY = 'ALLOW_MIXED_HEAD_BLOCK';