export default [
  {
    path: '/user',
    layout: false,
    routes: [{ name: '登录', path: '/user/login', component: './User/Login' }],
  },
  {
    path: '/user',
    layout: false,
    routes: [{ name: '注册', path: '/user/register', component: './User/Register' }],
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes:[
      { name: '查询表格', icon: 'table', path: '/admin/api_info', component: './ApiInfo' },
    ],
  },
  { path: '/', redirect: '/admin/api_info' },
  { path: '*', layout: false, component: './404' },
];
