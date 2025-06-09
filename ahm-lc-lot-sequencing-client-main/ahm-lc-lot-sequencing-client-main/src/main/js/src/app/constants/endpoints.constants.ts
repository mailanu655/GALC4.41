import { ConfigServiceProvider } from "../services/config.service.provider";

export const configServiceInstance = ConfigServiceProvider.useFactory();

export const BASE_URL = configServiceInstance['serviceUrl'];
export const GALC_URL = configServiceInstance['galcUrl'];
export const IS_DRAGABLE = configServiceInstance['isDragable'];
export const IS_SEND_TO_WELD_ON_ENABLED = configServiceInstance['isSentToWeldOnEnabled'];
export const DOWNLOAD_LOTS_URL = `${BASE_URL}/schedule/getGalcDownloadLots`;
export const BUILD_AHEAD_LOTS_URL = `${BASE_URL}/schedule/buildAheadLot`;
export const GET_LOTS_URL = `${BASE_URL}/schedule/getlots`;
export const FREEZE_LOTS_URL = `${BASE_URL}/schedule/freezelots`;
export const HOLD_LOTS_URL = `${BASE_URL}/schedule/holdLot`;
export const MOVE_LOT_BEHINND_LOT_URL = `${BASE_URL}/schedule/moveLotBehindLot`;
export const RELEASE_LOTS_URL = `${BASE_URL}/schedule/releaseLot`;
export const UNFREEZE_LOTS_URL = `${BASE_URL}/schedule/unfreezelots`;
export const SEND_TO_WELD_ON_URL = `${BASE_URL}/schedule/sendProductToWeldOn`;
export const GET_PLAN_CODES_URL = `${BASE_URL}/schedule/getPlanCodes`;
export const GET_PROCESS_LOCATIONS_URL = `${BASE_URL}/schedule/getProcessLocations`;
export const GET_ADD_NEW_CODE_URL = `${BASE_URL}/schedule/getAddNewLot`;
export const GET_PRODUCTS_URL = `${BASE_URL}/schedule/getproducts`;
export const GET_HOLD_LOTS_URL = `${BASE_URL}/schedule/getHoldLots`;
export const GET_BUILD_COMMENTS_URL = `${BASE_URL}/schedule/getBuildComments`;
export const GET_PRODUCT_SEND_STATUSES_URL = `${BASE_URL}/schedule/getProductSendStatuses`;
export const POST_SAVE_PRODUCT_SEND_STATUS_URL = `${BASE_URL}/schedule/saveProductSendStatus`;
export const POST_SAVE_LOTS_URL = `${BASE_URL}/schedule/saveLots`;
export const POST_SAVE_LOT_URL = `${BASE_URL}/schedule/saveLot`;
export const FIND_ALL_ACTIVE_PRODUCT_SPEC_CODES_ONLY_URL = `${GALC_URL}/findAllActiveProductSpecCodesOnly`;
