using Carer.Common;
using Carer.Model.DomainModels;
using Carer.Repository;
using Carer.Repository.Common;
using Carer.Service.Common;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Carer.Service
{
    class AdService : IAdService
    {
        private IAdRepository AdRepository;
        private IAppliedUserRepository appliedUserRepository;

        public AdService(IAdRepository adRepository, IAppliedUserRepository appliedUserRepository)
        {
            this.AdRepository = adRepository;
            this.appliedUserRepository = appliedUserRepository;
        }


        public async Task<List<AdDomainModel>> GetAllAdsAsync(AdFilter filter)
        {
            List<AdDomainModel> domainAds = await AdRepository.GetAllAdsAsync(filter);

            if (filter.ShowAppliedCount)
            {
                foreach (AdDomainModel adDomain in domainAds)
                {
                    if (adDomain.Active > 0)
                    {
                        adDomain.AppliedUser = await appliedUserRepository.GetAppliedUserAsync(adDomain.Id);
                    }
                    else
                    {
                        adDomain.AppliedUserCount = await appliedUserRepository.GetAppliedUserCountAsync(adDomain.Id);
                    }

                }
                domainAds.RemoveAll(adDomain => adDomain.Active == 4);
            }

            return domainAds;
        }
        public async Task<AdDomainModel> GetAdByIdAsync(Guid id, Guid userId)
        {
            AdDomainModel ad = await AdRepository.GetAdByIdAsync(id);
            if (await appliedUserRepository.GetAppliedUserStatusAsync(userId, ad.Id) != "")
            {
                ad.CurrentUserApplied = true;
            }
            return ad;
        }

        public async Task PostAdAsync(AdDomainModel Ad)
        {
            AdRepository.PostAdAsync(Ad);
        }

        public async Task PutAdAsync(AdDomainModel Ad)
        {
            AdRepository.PutAdAsync(Ad);
        }

        public async Task DeleteAdAsync(Guid id)
        {
            AdRepository.DeleteAdAsync(id);
        }
    }
}
