import { createRouter, createWebHistory } from "vue-router";
import { getSessionStorage } from "@/util/index.js";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/index.vue"),
  },
  {
    path: "/login-a",
    name: "LoginA",
    component: () => import("@/views/login/login-a.vue"),
  },
  {
    path: "/",
    name: "Layout",
    meta: {
      loginRequire: true,
    },
    component: () => import("@/views/layout/index.vue"),
    children: [
      {
        path: "index",
        name: "Index",
        component: () => import("@/views/system/index.vue"),
      },
      {
        path: "ownerList",
        name: "OwnerList",
        component: () => import("@/views/user/ownerList.vue"),
      },
      {
        path: "propertyList",
        name: "PropertyList",
        component: () => import("@/views/user/propertyList.vue"),
      },
      {
        path: "adminList",
        name: "AdminList",
        component: () => import("@/views/user/adminList.vue"),
      },
      {
        path: "districtList",
        name: "DistrictList",
        component: () => import("@/views/district/districtList.vue"),
      },
      {
        path: "buildingList",
        name: "BuildingList",
        component: () => import("@/views/building/buildingList.vue"),
      },
      {
        path: "houseList",
        name: "HouseList",
        component: () => import("@/views/building/houseList.vue"),
      },
      {
        path: "parkingList",
        name: "ParkingList",
        component: () => import("@/views/parking/parkingList.vue"),
      },
      {
        path: "feeList",
        name: "FeeList",
        component: () => import("@/views/fee/feeList.vue"),
      },
      {
        path: "announceList",
        name: "AnnounceList",
        component: () => import("@/views/announce/announceList.vue"),
      },
      {
        path: "repairList",
        name: "RepairList",
        component: () => import("@/views/repair/repairList.vue"),
      },
      {
        path: "complaintList",
        name: "ComplaintList",
        component: () => import("@/views/complaint/complaintList.vue"),
      },
    ],
  },
  {
    path: "/a",
    name: "LayoutA",
    meta: {
      loginRequire: true,
    },
    component: () => import("@/views/layout/layout-a.vue"),
    children: [
      {
        path: "index",
        name: "IndexA",
        component: () => import("@/views/system/system-a.vue"),
      },
      {
        path: "ownerList",
        name: "OwnerListA",
        component: () => import("@/views/user/ownerList.vue"),
      },
      {
        path: "propertyList",
        name: "PropertyListA",
        component: () => import("@/views/user/propertyList.vue"),
      },
      {
        path: "adminList",
        name: "AdminListA",
        component: () => import("@/views/user/adminList.vue"),
      },
      {
        path: "districtList",
        name: "DistrictListA",
        component: () => import("@/views/district/districtList.vue"),
      },
      {
        path: "buildingList",
        name: "BuildingListA",
        component: () => import("@/views/building/buildingList.vue"),
      },
      {
        path: "houseList",
        name: "HouseListA",
        component: () => import("@/views/building/houseList.vue"),
      },
      {
        path: "parkingList",
        name: "ParkingListA",
        component: () => import("@/views/parking/parkingList.vue"),
      },
      {
        path: "feeList",
        name: "FeeListA",
        component: () => import("@/views/fee/feeList.vue"),
      },
      {
        path: "announceList",
        name: "AnnounceListA",
        component: () => import("@/views/announce/announceList.vue"),
      },
      {
        path: "repairList",
        name: "RepairListA",
        component: () => import("@/views/repair/repairList.vue"),
      },
      {
        path: "complaintList",
        name: "ComplaintListA",
        component: () => import("@/views/complaint/complaintList.vue"),
      },
    ],
  },
  {
    path: "/",
    redirect: "/index",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from) => {
  // 临时跳过登录验证，直接进入系统
  // if (to?.meta?.loginRequire) {
  //   let token = getSessionStorage();
  //   if (!token) {
  //     return {
  //       path: "/login",
  //     };
  //   }
  // }
});

export default router;
