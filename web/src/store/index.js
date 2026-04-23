import { defineStore } from "pinia";

const usePiniaStore = defineStore("piniaStore", {
  state: () => {
    return {
      // 是否折叠菜单
      isCollapse: false,
      // 菜单数据
      ownerMenuList: [
        {
          id: 1,
          icon: "HomeFilled",
          name: "首页",
          path: "/index",
        },
        {
          id: 2,
          icon: "UserFilled",
          name: "用户信息",
          path: "/user",
          children: [
            {
              id: 2.1,
              icon: "List",
              name: "个人信息",
              path: "/ownerList",
            },
            {
              id: 2.2,
              icon: "List",
              name: "物业员工列表",
              path: "/propertyList",
            },
            {
              id: 2.3,
              icon: "List",
              name: "管理员列表",
              path: "/adminList",
            },
          ],
        },
        {
          id: 3,
          icon: "Help",
          name: "小区信息",
          path: "/districtList",
        },
        {
          id: 4,
          icon: "OfficeBuilding",
          name: "房屋信息",
          path: "/houseList",
        },
        {
          id: 5,
          icon: "Van",
          name: "车位信息",
          path: "/parkingList",
        },
        {
          id: 6,
          icon: "Money",
          name: "缴费信息",
          path: "/feeList",
        },
        {
          id: 7,
          icon: "ChatDotRound",
          name: "公告信息",
          path: "/announceList",
        },
        {
          id: 8,
          icon: "Setting",
          name: "维修信息",
          path: "/repairList",
        },
        {
          id: 9,
          icon: "Warning",
          name: "投诉信息",
          path: "/complaintList",
        },
      ],
      staffMenuList: [
        {
          id: 1,
          icon: "HomeFilled",
          name: "首页",
          path: "/index",
        },
        {
          id: 2,
          icon: "UserFilled",
          name: "用户管理",
          path: "/user",
          children: [
            {
              id: 2.1,
              icon: "List",
              name: "业主列表",
              path: "/ownerList",
            },
            {
              id: 2.2,
              icon: "List",
              name: "个人信息",
              path: "/propertyList",
            },
            {
              id: 2.3,
              icon: "List",
              name: "管理员列表",
              path: "/adminList",
            },
          ],
        },
        {
          id: 3,
          icon: "Help",
          name: "小区信息",
          path: "/districtList",
        },
        {
          id: 4,
          icon: "OfficeBuilding",
          name: "楼栋管理",
          path: "/building",
          children: [
            {
              id: 4.1,
              icon: "List",
              name: "楼栋列表",
              path: "/buildingList",
            },
            {
              id: 4.2,
              icon: "List",
              name: "房屋列表",
              path: "/houseList",
            },
          ],
        },
        {
          id: 5,
          icon: "Van",
          name: "车位管理",
          path: "/parking",
          children: [
            {
              id: 5.1,
              icon: "List",
              name: "车位列表",
              path: "/parkingList",
            },
          ],
        },
        {
          id: 6,
          icon: "Money",
          name: "缴费管理",
          path: "/fee",
          children: [
            {
              id: 6.1,
              icon: "List",
              name: "缴费列表",
              path: "/feeList",
            },
          ],
        },
        {
          id: 7,
          icon: "ChatDotRound",
          name: "公告管理",
          path: "/announce",
          children: [
            {
              id: 7.1,
              icon: "List",
              name: "公告列表",
              path: "/announceList",
            },
          ],
        },
        {
          id: 8,
          icon: "Setting",
          name: "维修管理",
          path: "/repair",
          children: [
            {
              id: 8.1,
              icon: "List",
              name: "维修列表",
              path: "/repairList",
            },
          ],
        },
        {
          id: 9,
          icon: "Warning",
          name: "投诉管理",
          path: "/complaint",
          children: [
            {
              id: 9.1,
              icon: "List",
              name: "投诉列表",
              path: "/complaintList",
            },
          ],
        },
      ],
      menuList: [
        {
          id: 1,
          icon: "HomeFilled",
          name: "首页",
          path: "/index",
        },
        {
          id: 2,
          icon: "UserFilled",
          name: "用户管理",
          path: "/user",
          children: [
            {
              id: 2.1,
              icon: "List",
              name: "业主列表",
              path: "/ownerList",
            },
            {
              id: 2.2,
              icon: "List",
              name: "物业员工列表",
              path: "/propertyList",
            },
            {
              id: 2.3,
              icon: "List",
              name: "管理员列表",
              path: "/adminList",
            },
          ],
        },
        {
          id: 3,
          icon: "Help",
          name: "小区管理",
          path: "/district",
          children: [
            {
              id: 3.1,
              icon: "List",
              name: "小区列表",
              path: "/districtList",
            },
          ],
        },
        {
          id: 4,
          icon: "OfficeBuilding",
          name: "楼栋管理",
          path: "/building",
          children: [
            {
              id: 4.1,
              icon: "List",
              name: "楼栋列表",
              path: "/buildingList",
            },
            {
              id: 4.2,
              icon: "List",
              name: "房屋列表",
              path: "/houseList",
            },
          ],
        },
        {
          id: 5,
          icon: "Van",
          name: "车位管理",
          path: "/parking",
          children: [
            {
              id: 5.1,
              icon: "List",
              name: "车位列表",
              path: "/parkingList",
            },
          ],
        },
        {
          id: 6,
          icon: "Money",
          name: "缴费管理",
          path: "/fee",
          children: [
            {
              id: 6.1,
              icon: "List",
              name: "缴费列表",
              path: "/feeList",
            },
          ],
        },
        {
          id: 7,
          icon: "ChatDotRound",
          name: "公告管理",
          path: "/announce",
          children: [
            {
              id: 7.1,
              icon: "List",
              name: "公告列表",
              path: "/announceList",
            },
          ],
        },
        {
          id: 8,
          icon: "Setting",
          name: "维修管理",
          path: "/repair",
          children: [
            {
              id: 8.1,
              icon: "List",
              name: "维修列表",
              path: "/repairList",
            },
          ],
        },
        {
          id: 9,
          icon: "Warning",
          name: "投诉管理",
          path: "/complaint",
          children: [
            {
              id: 9.1,
              icon: "List",
              name: "投诉列表",
              path: "/complaintList",
            },
          ],
        },
      ],
    };
  },
  actions: {
    setCollapse(value) {
      this.isCollapse = !value;
    },
  },
});

export default usePiniaStore;
